package ar.edu.itba.it.ss.sga_simulator.model;

import java.io.File;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ar.edu.itba.it.ss.sga_simulator.service.StatsService;

public class Server extends Thread {

	private StudentsQueue _queue;
	private StatsService _stats;
	private int _minimum_time_per_request;
	private int _maximum_time_per_request;
	private long _timeout;
	private boolean _log_enabled;
	private int _speed;
	private Random _random;

	public Server(StatsService stats, String xml_path, int speed) {
		_speed = speed;
		parseConfigurationFile(xml_path);
		_queue = StudentsQueue.getInstance();
		_stats = stats;
		_random = new Random();
	}

	public void run() {
		_stats.start();
		log("SERVER> Empezando simulación...");
		long start = System.currentTimeMillis();
		while (!_queue.finished()) {
			try {
				attendRequest();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		log("SERVER> Matriculación SGA completada.");
		log("SERVER> Alumnos matriculados: " + _stats.matriculatedStudents());
		log("SERVER> Timeouts: " + _stats.timeouts());
		_stats.duration((System.currentTimeMillis() - start) * _speed);
		_stats.printDuration();
		log("SERVER> Alumnos satisfechos: " + _stats.satisfiedStudentsAmount());
	}

	private void attendRequest() throws InterruptedException {
		if (_queue.isEmpty()) {
			return;
		} else {
			Student student = _queue.poll();
			if (System.currentTimeMillis() - student.queueTime() > _timeout) {
				_stats.addTimeout();
				student.timeout();
				log("SERVER> Legajo: " + student.id() + " >> TIMEOUT--");
				if (student.consecutiveTimeouts() == Student.MAX_CONSECUTIVE_TIMEOUTS) {
					_queue.restoreStudentToPool(student);
				} else {
					_queue.add(student);
				}
			} else {
				log("SERVER> Atendiendo legajo: " + student.id());
				int time_per_request = _random
						.nextInt(_maximum_time_per_request
								- _minimum_time_per_request + 1)
						- _minimum_time_per_request;
				sleep(time_per_request);// Demora un tiempo _time_per_request
				// en
				// atender la solicitud
				// del estudiante
				student.resetTimeouts();
				attend(student);
			}
		}
	}

	private void log(String message) {
		if (_log_enabled) {
			System.out.println(message);
		}
	}

	private void attend(Student student) {
		Action student_action = student.getAction();
		if (student_action != null) {
			Course course = student_action.course();
			if (course != null) {
				if (student.canCourse(course)) {
					student.addMatriculatedCourse(course);
				} else {
					student.addNotMatriculatedCourse(course);
				}
			}
			student.consumeAction();
		}
		checkFinishedMatriculating(student);
	}

	private void checkFinishedMatriculating(Student student) {
		if (!student.hasFinishedMatriculating()) {
			_queue.add(student);
		} else {
			addMatriculatedStudent(student);
		}
	}

	private void addMatriculatedStudent(Student student) {
		log("SERVER> Legajo:" + student.id() + " >> TERMINO--");
		_stats.addMatriculatedStudent();
		if (student.isSatisfied()) {
			_stats.addStatisfiedStudent();
		}
	}

	private void parseConfigurationFile(String xml_path) {
		try {
			File fXmlFile = new File(xml_path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			Element server = (Element) doc.getElementsByTagName("server").item(
					0);
			_timeout = getIntValue(server, "timeout") / _speed;
			_minimum_time_per_request = getIntValue(server,
					"minimum-time-per-request") / _speed;
			_maximum_time_per_request = getIntValue(server,
					"maximum-time-per-request") / _speed;
			_log_enabled = getBoolValue(server, "log-enabled");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Integer getIntValue(Element server, String attribute) {
		return Integer.parseInt(server.getElementsByTagName(attribute).item(0)
				.getTextContent());
	}

	private boolean getBoolValue(Element server, String attribute) {
		return Boolean.parseBoolean(server.getElementsByTagName(attribute)
				.item(0).getTextContent());
	}
}
