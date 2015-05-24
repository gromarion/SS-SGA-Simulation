package ar.edu.itba.it.ss.sga_simulator.web.api;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

import ar.edu.itba.it.ss.sga_simulator.model.Carreer;
import ar.edu.itba.it.ss.sga_simulator.model.CarreerParser;
import ar.edu.itba.it.ss.sga_simulator.model.Server;
import ar.edu.itba.it.ss.sga_simulator.model.Student;
import ar.edu.itba.it.ss.sga_simulator.model.StudentsQueue;
import ar.edu.itba.it.ss.sga_simulator.service.MatriculationService;
import ar.edu.itba.it.ss.sga_simulator.service.StatsService;

@RestController
public class SimulatorController {
	
	@Autowired
	private MatriculationService matriculationService;
	
	@Autowired
	private StatsService stats;
	
	@RequestMapping("/start")
	public void start() {
		 Carreer carreer = CarreerParser.parse("SoftwareEngineering.xml");
		 List<Student> students = Student.getFactory().create(carreer, 1000);
		 matriculationService.prepareDesiredCourses(carreer, students);
		 int speed = speed("SimulationConfiguration.xml");
		 stats.setSpeed(speed);
		 StudentsQueue queue = StudentsQueue.getInstance();
		 queue.initialize("QueueConfiguration.xml", students);
		 new Server(stats, "ServerConfiguration.xml", speed).start();
		 queue.start();
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
			return 1;
		}
	}
}
