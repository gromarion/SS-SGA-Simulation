package ss.SGA_Simulation;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class StudentsQueue extends Thread {

	private ConcurrentLinkedQueue<Student> _queue;
	private CopyOnWriteArrayList<Student> _students;
	private int _student_arrival_time;
	private boolean _log_enabled;
	private static StudentsQueue _instance;

	public void initialize(String xml_file, List<Student> students, int speed) {
		_queue = new ConcurrentLinkedQueue<Student>();
		_students = new CopyOnWriteArrayList<Student>();
		_students.addAll(students);
		parseConfigurationFile(xml_file, speed);
		Stats.getInstance().setTotalStudents(_students.size());
	}

	private void parseConfigurationFile(String xml_file, int speed) {
		try {
			File fXmlFile = new File(xml_file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			Element server = (Element) doc.getElementsByTagName("queue")
					.item(0);
			_student_arrival_time = getIntValue(server, "client-arrival-time")
					/ speed;
			_log_enabled = getBoolValue(server, "log-enabled");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static StudentsQueue getInstance() {
		if (_instance == null) {
			_instance = new StudentsQueue();
		}
		return _instance;
	}

	private StudentsQueue() {
	}

	public void run() {
		Random random = new Random();
		while (!finished()) {
			try {
				sleep(_student_arrival_time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (_students.size() > 0) {
				int random_student_index = random.nextInt(_students.size());
				Student student = _students.get(random_student_index);
				add(student);
				_students.remove(student);
			}
		}
	}

	public boolean isEmpty() {
		return _queue.isEmpty();
	}

	public boolean finished() {
		return _queue.size() == 0 && _students.size() == 0;
	}

	public Student poll() {
		return _queue.poll();
	}

	public boolean add(Student student) {
		student.setQueueTime(System.currentTimeMillis());
		if (_log_enabled) {
			System.out.println("QUEUE> Legajo:" + student.id()
					+ " Agregado a la cola.");
		}
		return _queue.add(student);
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
