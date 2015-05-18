package ss.SGA_Simulation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Server extends Thread {

	private StudentsQueue _queue;
	private long _time_per_request;
	private long _timeout;
	private List<Student> _matriculated_students;

	public Server(String xml_path) {
		parseConfigurationFile(xml_path);
		_matriculated_students = new ArrayList<Student>();
		_queue = StudentsQueue.getInstance();
	}

	public void run() {
		while (!_queue.finished()) {
			try {
				attendRequest();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("SGA Matriculation has finished!!!");
		System.out.println("Matriculated students = "
				+ _matriculated_students.size());
		System.out.println(_matriculated_students);
	}

	private void attendRequest() throws InterruptedException {
		if (_queue.isEmpty()) {
			sleep(1000);
			System.out.print(".");
			return;
		} else {
			Student student = _queue.poll();
			System.out.print("\nAttending " + student.id());
			sleep(_time_per_request);// Demora un tiempo _time_per_request
										// en
			// atender la solicitud
			// del estudiante
			if (System.currentTimeMillis() - student.queueTime() > _timeout) {
				_queue.add(student);
				System.out.println(student.id() + " TIMEOUT");
			} else {
				attend(student);
			}
		}
	}

	private void attend(Student student) {
		Course course = student.getADesiredCourse();
		if (course != null) {
			if (student.canCourse(course)) {
				student.addMatriculatedCourse(course);
			} else {
				student.addNotMatriculatedCourse(course);
			}
			if (!student.hasFinishedMatriculating()) {
				_queue.add(student);
			} else {
				_matriculated_students.add(student);
				System.out.print("\nStudent " + student.id()
						+ " finished matriculating!!!");
			}
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
			_timeout = getValue(server, "timeout");
			_time_per_request = getValue(server, "time-per-request");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Integer getValue(Element server, String attribute) {
		return Integer.parseInt(server.getElementsByTagName(attribute).item(0)
				.getTextContent());
	}
}
