package ss.SGA_Simulation;

import java.util.ArrayList;
import java.util.List;

public class StudentsCreator {

	public static List<Student> create(Carreer carreer, int students_amount) {
		List<Student> students = new ArrayList<Student>();
		for (int i = 0; i < students_amount; i++) {
			students.add(new Student(i));
		}
		return simulateCarreer(carreer, students);
	}

	private static List<Student> simulateCarreer(Carreer carreer,
			List<Student> students) {
		int batch = students.size() / carreer.quarters();
		int carreer_quarter = 1;
		while (carreer_quarter <= carreer.quarters()) {
			for (int i = 0; i < batch * carreer_quarter; i++) {
				simulateQuarter(carreer, students.get(i));
			}
			carreer_quarter++;
		}
		return students;
	}

	private static void simulateQuarter(Carreer carreer, Student student) {
		List<Course> courses = Matriculation.fetchCourses(carreer, student);
		for (Course course : courses) {
			student.course(course);
		}
		student.promote(carreer);
	}
}
