package ar.edu.itba.it.ss.sga_simulator.model;

import java.io.File;
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ar.edu.itba.it.ss.sga_simulator.criteria.DelayedStudentsFirst;
import ar.edu.itba.it.ss.sga_simulator.criteria.OlderStudentsFirst;
import ar.edu.itba.it.ss.sga_simulator.criteria.OnScheduleStudentsFirst;
import ar.edu.itba.it.ss.sga_simulator.criteria.YoungerStudentsFirst;

@Service
public class ConfigurationService {

	private int _speed;
	private int _matriculation_days;
	private Comparator<Student> _criteria;
	private static final int DEFAULT_INT_VALUE = 1;
	private boolean _log_enabled;
	private boolean _divide_students_in_groups;
	private String _criteriaName;

	public ConfigurationService() {
		String xml_file = "SimulationConfiguration.xml";
		try {
			Element simulation = getDocument(xml_file);
			speed(parseInt(simulation, "speed"));
			matriculationDays(parseInt(simulation, "matriculation-days"));
			criteria(parseString(simulation, "criteria"));
			logEnabled(parseBoolean(simulation, "log-enabled"));
			divideStudentsInGroups(parseBoolean(simulation,
					"divide-students-in-groups"));
		} catch (Exception e) {
			_speed = DEFAULT_INT_VALUE;
			_matriculation_days = DEFAULT_INT_VALUE;
			_criteria = new OlderStudentsFirst();
		}
	}

	public void divideStudentsInGroups(boolean divide_students_in_groups) {
		_divide_students_in_groups = divide_students_in_groups;
	}

	public void logEnabled(boolean log_enabled) {
		_log_enabled = log_enabled;		
	}

	public void criteria(String criteriaName) {
		_criteriaName = criteriaName;
		switch (_criteriaName) {
		case "younger":
			_criteria = new YoungerStudentsFirst();
			break;
		case "onschedule":
			_criteria = new OnScheduleStudentsFirst();
			break;
		case "delayed":
			_criteria = new DelayedStudentsFirst();
			break;
		case "older":
		default:
			_criteria = new OlderStudentsFirst();
		}
	}

	public void matriculationDays(int matriculationDays) {
		_matriculation_days = matriculationDays;		
	}

	public void speed(int speed) {
		_speed = speed;
	}

	public int speed() {
		return _speed;
	}

	public boolean logEnabled() {
		return _log_enabled;
	}
	
	public boolean divideStudentsInGroups() {
		return _divide_students_in_groups;
	}

	public int matriculationDays() {
		return _matriculation_days;
	}

	public Comparator<Student> criteria() {
		return _criteria;
	}

	private static Element getDocument(String xml_file) throws Exception {
		File fXmlFile = new File(xml_file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		Element simulation = (Element) doc.getElementsByTagName("simulation")
				.item(0);
		return simulation;
	}
	
	private static String parseString(Element root, String tag) {
		return root.getElementsByTagName(tag).item(0).getTextContent();
	}

	private static int parseInt(Element root, String tag) {
		return Integer.parseInt(root.getElementsByTagName(tag).item(0)
				.getTextContent());
	}

	private static boolean parseBoolean(Element element, String tag) {
		return Boolean.parseBoolean(element.getElementsByTagName(tag).item(0)
				.getTextContent());
	}

	public String criteriaName() {
		return _criteriaName;
	}
}
