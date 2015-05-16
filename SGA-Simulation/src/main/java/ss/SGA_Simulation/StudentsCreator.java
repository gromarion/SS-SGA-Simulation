package ss.SGA_Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StudentsCreator {

	private static int MIN_CREDITS = 15;
	private static int MAX_CREDITS = 33;

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
		int total_credits = calculateCredits(carreer, student);
		List<Course> courses = fetchCourses(carreer, student, total_credits);
		for (Course course : courses) {
			student.course(course);
		}
		student.promote(carreer);
	}

	private static List<Course> fetchCourses(Carreer carreer, Student student,
			int total_credits) {
		int credits = 0;
		int current_year = student.currentYear();
		int current_quarter = student.currentQuarter();
		List<Course> courses = new ArrayList<Course>();
		while (credits <= total_credits
				&& courses.size() + student.passedCoursesAmount() <= carreer
						.coursesAmount() && current_year <= carreer.years()) {
			Map<Integer, Course> possible_courses = carreer.courses(
					current_year).get(current_quarter);
			for (Integer course_code : possible_courses.keySet()) {
				Course possible_course = possible_courses.get(course_code);
				if (student.canCourse(possible_course)) {
					courses.add(possible_course);
					credits += possible_course.credits();
				}
			}

			current_quarter++;
			if (current_quarter == 3) {
				current_year++;
				current_quarter = 1;
			}
		}
		return courses;
	}

	// Estima la cantidad de creditos que va a cursar un alumno. Si se encuentra
	// en el 1er cuatrimestre del primer año y no recurso ninguna materia,
	// entonces cursara todo el primer cuatrimestre. Si no, se estimara la
	// cantidad de creditos que desea cursar.
	private static int calculateCredits(Carreer carreer, Student student) {
		if (student.isFreshMan()) {
			return carreer.creditsAmount(1, 1);
		}
		return new Random().nextInt((MAX_CREDITS - MIN_CREDITS) + 1)
				+ MIN_CREDITS;
	}
}
