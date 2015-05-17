package ss.SGA_Simulation;

import java.util.List;

public class Simulator {
	public static void main(String[] args) {
		Carreer carreer = CarreerParser.parse("SoftwareEngineering.xml");
		List<Student> students = StudentsCreator.create(carreer, 100);
		System.out.println(carreer);
		for (Student student : students) {
			System.out.println(student);
		}
		Matriculation.prepareDesiredCourses(carreer, students);
		StudentsQueue queue = new StudentsQueue(students);
		queue.start();
		new Server(500, 1000, queue).start();
	}
}
