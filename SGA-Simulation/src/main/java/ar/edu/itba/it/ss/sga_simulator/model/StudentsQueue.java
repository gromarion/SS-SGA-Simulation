package ar.edu.itba.it.ss.sga_simulator.model;

import java.io.File;
import java.util.List;
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
	private int _students_amount_per_day;
	private boolean _log_enabled;
	private double[] _lambdas; // distribution of students per hour

	private static final int MAX_MATRICULATION_DAYS = 5;

	private static void createInstance(StatsService stats) {
		_instance = new StudentsQueue(stats);
	}

	public void initialize(String xml_file, List<List<Student>> students) {
		_queue = new ConcurrentLinkedQueue<Student>();
		_students = students;
		_students_amount_per_day = students.get(0).size();
		parseConfigurationFile(xml_file);
		_stats.setTotalStudents(_students.size());
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
			_lambdas = parseLambdas(queue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private double[] parseLambdas(Element element) {
		NodeList lambdas = ((Element) (element.getElementsByTagName("means")
				.item(0))).getElementsByTagName("mean");
		int lambdas_amount = lambdas.getLength();
		double[] ans = new double[lambdas_amount];
		for (int i = 0; i < lambdas_amount; i++) {
			ans[i] = Double.parseDouble(lambdas.item(i).getTextContent());
		}
		return ans;
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
		while (_stats.day() < MAX_MATRICULATION_DAYS && thereAreStudentsLeft()) {
			int current_day = _stats.day();
			if (current_day >= MAX_MATRICULATION_DAYS) {
				break;
			}
			_stats.updateDaytime(_lambdas.length);
			int students_left = _students.get(current_day).size();
			if (students_left > 0) {
				try {
					long wait = (long) Math
							.ceil(exponentialDistributionGenerator()
									* StatsService.MILLIS_IN_A_MINUTE
									/ _stats.speed());
					sleep(wait);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int random_student_index = random.nextInt(students_left);
				Student student = _students.get(current_day).get(
						random_student_index);
				add(student);
				_students.get(current_day).remove(student);
			}
		}
		while (!finished()) {
			_stats.updateDaytime(_lambdas.length);
		}
	}

	public boolean isEmpty() {
		return _queue.isEmpty();
	}

	public boolean finished() {
		if (_stats.day() >= MAX_MATRICULATION_DAYS) {
			return true;
		}
		return _queue.size() == 0 && !thereAreStudentsLeft();
	}

	private boolean thereAreStudentsLeft() {
		boolean more_students = true;
		for (List<Student> students : _students) {
			more_students |= students.size() > 0;
		}
		return more_students;
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

	private boolean getBoolValue(Element queue, String attribute) {
		return Boolean.parseBoolean(queue.getElementsByTagName(attribute)
				.item(0).getTextContent());
	}

	private double exponentialDistributionGenerator() {
		Random random = new Random();
		double mean = 60 / (_lambdas[_stats.daytime()] * _students_amount_per_day);
		double lambda = 1 / mean;
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
}
