package ss.SGA_Simulation;

import java.util.List;

public class Simulator {
	public static void main(String[] args) {
		Carreer carreer = CarreerParser.parse("SoftwareEngineering.xml");
		List<Student> students = StudentsCreator.create(carreer, 10);
		Matriculation.prepareDesiredCourses(carreer, students);
		StudentsQueue queue = StudentsQueue.getInstance();
		queue.initialize("QueueConfiguration.xml", students);
		new Server("ServerConfiguration.xml").start();
		queue.start();
	}
}
