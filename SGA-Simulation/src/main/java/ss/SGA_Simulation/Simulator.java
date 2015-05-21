package ss.SGA_Simulation;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Simulator {
	public static void main(String[] args) {
		 Carreer carreer = CarreerParser.parse("SoftwareEngineering.xml");
		 List<Student> students = StudentsCreator.create(carreer, 1);
		 Matriculation.prepareDesiredCourses(carreer, students);
		 int speed = speed("SimulationConfiguration.xml");
		 StudentsQueue queue = StudentsQueue.getInstance();
		 queue.initialize("QueueConfiguration.xml", students, speed);
		 new Server("ServerConfiguration.xml", speed).start();
		 queue.start();
	}

	public static int speed(String xml_file) {
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
