package ar.edu.itba.it.ss.sga_simulator.model;

import java.io.File;
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ar.edu.itba.it.ss.sga_simulator.criteria.DelayedStudentsFirst;
import ar.edu.itba.it.ss.sga_simulator.criteria.OlderStudentsFirst;
import ar.edu.itba.it.ss.sga_simulator.criteria.OnScheduleStudentsFirst;
import ar.edu.itba.it.ss.sga_simulator.criteria.YoungerStudentsFirst;

public class SimulationConfiguration {

	private int _speed;
	private int _matriculation_days;
	private Comparator<Student> _criteria;
	private static final int DEFAULT_INT_VALUE = 1;
	private boolean _log_enabled;

	public SimulationConfiguration(String xml_file) {
		try {
			Element simulation = getDocument(xml_file);
			_speed = parseInt(simulation, "speed");
			_matriculation_days = parseInt(simulation, "matriculation-days");
			_criteria = parseCriteria(simulation);
			_log_enabled = parseBoolean(simulation);
		} catch (Exception e) {
			_speed = DEFAULT_INT_VALUE;
			_matriculation_days = DEFAULT_INT_VALUE;
			_criteria = new OlderStudentsFirst();
		}
	}

	public int speed() {
		return _speed;
	}
	
	public boolean logEnabled() {
		return _log_enabled;
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

	private static int parseInt(Element root, String tag) {
		return Integer.parseInt(root.getElementsByTagName(tag).item(0)
				.getTextContent());
	}

	private static Comparator<Student> parseCriteria(Element root) {
		switch (root.getElementsByTagName("criteria").item(0).getTextContent()) {
		case "older":
			return new OlderStudentsFirst();
		case "younger":
			return new YoungerStudentsFirst();
		case "onschedule":
			return new OnScheduleStudentsFirst();
		case "delayed":
			return new DelayedStudentsFirst();
		default:
			return new OlderStudentsFirst();
		}
	}

	private static boolean parseBoolean(Element element) {
		return Boolean.parseBoolean(element.getElementsByTagName("log-enabled")
				.item(0).getTextContent());
	}
}
