package ar.edu.itba.it.ss.sga_simulator.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ar.edu.itba.it.ss.sga_simulator.service.StatsService;

public class StudentsQueue extends Thread {

	private static StudentsQueue _instance;

	private ConcurrentLinkedQueue<Student> _queue;
	private StatsService _stats;
	private List<List<Student>> _students;
	private boolean _log_enabled;
	private Map<String, List<Double>> _lambdas; // distribution of students per
												// day, per hour
	private int _matriculation_days;
	private int _max_students_in_queue;
	private long _lesser_wait = Long.MAX_VALUE;
	private boolean _students_divided_in_groups;

	private static void createInstance(StatsService stats) {
		_instance = new StudentsQueue(stats);
	}

	public void initialize(String xml_file, List<List<Student>> students,
			int matriculation_days, boolean students_divided_in_groups) {
		_queue = new ConcurrentLinkedQueue<Student>();
		_students = students;
		_lambdas = new HashMap<String, List<Double>>();
		_matriculation_days = matriculation_days;
		_students_divided_in_groups = students_divided_in_groups;
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
			Element queue = (Element) doc.getElementsByTagName("queue").item(0);
			_log_enabled = getBoolValue(queue, "log-enabled");
			NodeList days = queue.getElementsByTagName("day");
			for (int i = 0; i < days.getLength(); i++) {
				parseLambdas((Element) days.item(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseLambdas(Element element) {
		String day_name = element.getAttribute("id");
		NodeList lambdas = ((Element) (element.getElementsByTagName("times")
				.item(0))).getElementsByTagName("time");
		List<Double> lambas_per_day = new ArrayList<Double>();
		for (int i = 0; i < lambdas.getLength(); i++) {
			lambas_per_day.add(Double.parseDouble(lambdas.item(i)
					.getTextContent()));
		}
		_lambdas.put(day_name, lambas_per_day);
	}

	public static StudentsQueue getInstance() {
		if (_instance == null) {
			throw new IllegalStateException("Should be instantiated");
		}
		return _instance;
	}

	private StudentsQueue(StatsService stats) {
		_stats = stats;
	}

	// Las medias están puestas en el archivo de configuración en unidades de
	// HORA. Es decir, donde dice <mean>NUMBER</mean>, NUMBER es el número de
	// alumnos que llegan por HORA en promedio. Las cuentas que se hacen para
	// ver si ingresó o no un alumno al sistema es calcular la probabilidad de
	// tener que esperar elapsedTimeHours(last_student_time), generar un numero
	// aleatorio, y verificar si este útimo es menor a la probabilidad de
	// esperar esa cantidad de tiempo. Si es menor, ingresa un alumno al
	// sistema, si no, se vuelve a hacer lo mismo. Cada vez que pasa una hora,
	// se actualiza el valor de lambda para la función de distribución
	// exponencial.
	public void run() {
		Random random = new Random();
		while (_stats.day() < _matriculation_days && thereAreStudentsLeft()) {
			// log("Students in queue = " + _queue.size());
			_stats.updateDaytime();
			int current_day = _stats.day();
			int student_group;
			if (_students_divided_in_groups) {
				student_group = current_day;
			} else {
				student_group = 0;
			}
			if (current_day >= _matriculation_days) {
				break;
			}
			int matriculation_duration_per_day = _lambdas.get(_stats.dayName())
					.size();
			int matriculation_starting_time = StatsService.HOURS_IN_A_DAY
					- matriculation_duration_per_day;
			if (_stats.daytime() >= matriculation_starting_time
					&& _stats.daytime() < matriculation_starting_time
							+ matriculation_duration_per_day) {
				_stats.updateDaytime();
				int students_left = _students.get(student_group).size();
				if (students_left > 0) {
					try {
						long wait = (long) Math
								.ceil(exponentialDistributionGenerator() * 1000
										/ _stats.speed());
						if (wait < _lesser_wait) {
							_lesser_wait = wait;
						}
						sleep(wait);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					int random_student_index = random.nextInt(students_left);
					Student student = _students.get(student_group).get(
							random_student_index);
					add(student);
					_students.get(student_group).remove(student);
				}
			}
		}
		while (!finished()) {
			_stats.updateDaytime();
		}
		log("Max students in queue = " + _max_students_in_queue);
		log("Lesser wait = " + _lesser_wait);
	}

	public boolean isEmpty() {
		return _queue.isEmpty();
	}

	public boolean finished() {
		if (_stats.day() >= _matriculation_days) {
			return true;
		}
		return _queue.size() == 0 && !thereAreStudentsLeft();
	}

	private boolean thereAreStudentsLeft() {
		boolean more_students = false;
		for (List<Student> students : _students) {
			more_students = more_students || (students.size() > 0);
		}
		return more_students;
	}

	public Student poll() {
		return _queue.poll();
	}

	public boolean add(Student student) {
		student.setQueueTime(System.currentTimeMillis());
//		log("QUEUE> Legajo:" + student.id() + " Agregado a la cola.");
		if (_queue.size() > _max_students_in_queue) {
			_max_students_in_queue = _queue.size();
		}
		return _queue.add(student);
	}

	private void log(String message) {
		if (_log_enabled) {
			System.out.println(message);
		}
	}

	private boolean getBoolValue(Element queue, String attribute) {
		return Boolean.parseBoolean(queue.getElementsByTagName(attribute)
				.item(0).getTextContent());
	}

	private double exponentialDistributionGenerator() {
		Random random = new Random();
		int matriculation_duration_per_day = _lambdas.get(_stats.dayName())
				.size();
		double time = _lambdas.get(_stats.dayName())
				.get(-(StatsService.HOURS_IN_A_DAY
						- matriculation_duration_per_day - _stats.daytime()));
		double lambda = 1 / time;
		return Math.log(1 - random.nextDouble()) / (-lambda);
	}

	@Component
	public static class StudentsQueueInjector {
		@Autowired
		private StatsService stats;

		@PostConstruct
		public void postConstruct() {
			StudentsQueue.createInstance(stats);
		}
	}

	public void restoreStudentToPool(Student student) {
		int group_number = 0;
		if (_students_divided_in_groups) {
			group_number = _stats.day();
		}
		_students.get(group_number).add(student);
	}
}
