package ss.SGA_Simulation;

import java.util.ArrayList;
import java.util.List;

public class StudentsCreator {

	public static List<Student> create(Carreer carreer, int amount, int batch_size) {
		List<Student> students = new ArrayList<Student>();
		for (int i = 0; i < amount; i++) {
			students.add(new Student(null, 1, i));
		}
		return simulateCarreer(carreer, students, batch_size);
	}

	private static List<Student> simulateCarreer(Carreer carreer,
			List<Student> students, int batch_size) {
		int batch = students.size()/batch_size;
		return null;
	}
}
