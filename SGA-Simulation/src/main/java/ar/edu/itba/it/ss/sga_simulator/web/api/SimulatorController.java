package ar.edu.itba.it.ss.sga_simulator.web.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

import ar.edu.itba.it.ss.sga_simulator.model.Carreer;
import ar.edu.itba.it.ss.sga_simulator.model.CarreerParser;
import ar.edu.itba.it.ss.sga_simulator.model.OlderStudentsFirst;
import ar.edu.itba.it.ss.sga_simulator.model.Server;
import ar.edu.itba.it.ss.sga_simulator.model.Student;
import ar.edu.itba.it.ss.sga_simulator.model.StudentsQueue;
import ar.edu.itba.it.ss.sga_simulator.service.MatriculationService;
import ar.edu.itba.it.ss.sga_simulator.service.StatsService;

@RestController
public class SimulatorController {

	@Autowired
	private MatriculationService _matriculationService;
	@Autowired
	private StatsService _stats;
	private static final int DEFAULT_SPEED = 1;

	@RequestMapping("/start")
	public void start() {
		Carreer carreer = CarreerParser.parse("SoftwareEngineering.xml");
		List<Student> students = Student.getFactory().create(carreer, 1000);
		_matriculationService.prepareDesiredCourses(carreer, students);
		// <NUEVO>
		List<List<Student>> divided_students = divideStudentsByCriteria(
				students, new OlderStudentsFirst(), carreer.years());
		// </NUEVO>
		int speed = speed("SimulationConfiguration.xml");
		_stats.setSpeed(speed);
		StudentsQueue queue = StudentsQueue.getInstance();
		queue.initialize("QueueConfiguration.xml", divided_students);
		new Server(_stats, "ServerConfiguration.xml", speed).start();
		queue.start();
	}

	private List<List<Student>> divideStudentsByCriteria(
			List<Student> students, Comparator<Student> criteria,
			int carreer_years) {
		List<List<Student>> ans = new ArrayList<List<Student>>();
		int batch_size = students.size() / carreer_years;
		Collections.sort(students, criteria);
		for (int i = 0; i < batch_size; i++) {
			List<Student> students_in_year_i = new ArrayList<Student>();
			for (int j = 0; j < carreer_years; j++) {
				students_in_year_i.add(students.get(i + j));
			}
			ans.add(students_in_year_i);
		}
		return ans;
	}

	private int speed(String xml_file) {
		try {
			File fXmlFile = new File(xml_file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			return Integer.parseInt(doc.getElementsByTagName("speed").item(0)
					.getTextContent());
		} catch (Exception e) {
			e.printStackTrace();
			return DEFAULT_SPEED;
		}
	}
}
