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
import org.w3c.dom.NodeList;

public class StudentsQueue extends Thread {

	private ConcurrentLinkedQueue<Student> _queue;
	private CopyOnWriteArrayList<Student> _students;
	private int _students_amount;
	private boolean _log_enabled;
	private int _daytime;
	private double[] _lambdas;
	private static final int MILLIS_IN_AN_HOUR = 3600000;
	private int _speed;
	private long _start;
	private static StudentsQueue _instance;

	public void initialize(String xml_file, List<Student> students, int speed) {
		_queue = new ConcurrentLinkedQueue<Student>();
		_students = new CopyOnWriteArrayList<Student>();
		_students.addAll(students);
		_students_amount = students.size();
		parseConfigurationFile(xml_file, speed);
		Stats.getInstance().setTotalStudents(_students.size());
		_speed = speed;
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
			_instance = new StudentsQueue();
		}
		return _instance;
	}

	private StudentsQueue() {
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
		_start = System.currentTimeMillis();
		while (_students.size() > 0) {
			updateDaytime();
			try {
				long wait = (long) Math.ceil((exponentialDistributionGenerator() * 60000/_speed));
				System.out.println(wait);
				sleep(wait);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int random_student_index = random.nextInt(_students.size());
			Student student = _students.get(random_student_index);
			add(student);
			_students.remove(student);
		}
		while (!finished()) {
			updateDaytime();
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

	private void updateDaytime() {
		int current_daytime = (int) ((elapsedTime(_start)) / MILLIS_IN_AN_HOUR)
				% _lambdas.length;
		if (current_daytime != _daytime) {
			_daytime = current_daytime;
			System.out.println(_daytime + "hs. Alumnos restantes: "
					+ (_queue.size() + _students.size()));
		}
	}

	private long elapsedTime(long start) {
		return (System.currentTimeMillis() - start) * _speed;
	}

	private boolean getBoolValue(Element server, String attribute) {
		return Boolean.parseBoolean(server.getElementsByTagName(attribute)
				.item(0).getTextContent());
	}

	private double exponentialDistributionGenerator() {
		Random random = new Random();
		double mean = 60 / (_lambdas[_daytime] * _students_amount);
		double lambda = 1/mean;
		return Math.log(1 - random.nextDouble()) / (-lambda);
	}
}
