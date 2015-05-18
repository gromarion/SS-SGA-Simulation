package ss.SGA_Simulation;

import java.io.File;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class StudentsQueue extends Thread {

	private Queue<Student> _queue;
	private List<Student> _students;
	private int _student_arrival_time;
	private boolean _log_enabled;
	private static StudentsQueue instance;
	
	public void initialize(String xml_file, List<Student> students) {
		_queue = new PriorityBlockingQueue<Student>(students.size());
		_students = students;
		parseConfigurationFile(xml_file);
	}
	
	private void parseConfigurationFile(String xml_file) {
		try {
			File fXmlFile = new File(xml_file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			Element server = (Element) doc.getElementsByTagName("queue").item(
					0);
			_student_arrival_time = getIntValue(server, "client-arrival-time");
			_log_enabled = getBoolValue(server, "log-enabled");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static StudentsQueue getInstance() {
		if (instance == null) {
			instance = new StudentsQueue();
		}
		return instance;
	}
	
	private StudentsQueue() {
	}
	
	public void run() {
		Random random = new Random();
		while(!finished()) {
			try {
				sleep(_student_arrival_time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			double probability = random.nextDouble();
			if (_students.size() > 0 && probability > 0.5) {
				int random_student_index = random.nextInt(_students.size());
				Student student = _students.remove(random_student_index);
				add(student);
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
			System.out.println("QUEUE> Legajo:" + student.id() + " Agregado a la cola.");			
		}
		return _queue.add(student);
	}
	
	private Integer getIntValue(Element server, String attribute) {
		return Integer.parseInt(server.getElementsByTagName(attribute).item(0)
				.getTextContent());
	}

	private boolean getBoolValue(Element server, String attribute) {
		return Boolean.parseBoolean(server.getElementsByTagName(attribute).item(0)
				.getTextContent());
	}
}
