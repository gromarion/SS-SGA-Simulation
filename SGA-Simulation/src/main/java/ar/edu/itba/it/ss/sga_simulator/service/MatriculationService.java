package ar.edu.itba.it.ss.sga_simulator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import ar.edu.itba.it.ss.sga_simulator.model.Carreer;
import ar.edu.itba.it.ss.sga_simulator.model.Course;
import ar.edu.itba.it.ss.sga_simulator.model.Student;

@Service
public class MatriculationService {
	
	private static final int MIN_CREDITS = 15;
	private static final int MAX_CREDITS = 33;
	
	public void prepareDesiredCourses(Carreer carreer, List<Student> students) {
		for (Student student : students) {
			student.addDesiredCourses(fetchCourses(carreer, student));
		}
	}

	public List<Course> fetchCourses(Carreer carreer, Student student) {
		int credits = 0;
		int current_year = student.currentYear();
		int current_quarter = student.currentQuarter();
		int total_credits = calculateCredits(carreer, student);
		List<Course> courses = new ArrayList<Course>();
		while (credits <= total_credits
				&& courses.size() + student.passedCoursesAmount() <= carreer
						.coursesAmount() && current_year <= carreer.years()) {
			Map<Integer, Course> possible_courses = carreer.courses(
					current_year).get(current_quarter);
			for (Integer course_code : possible_courses.keySet()) {
				Course possible_course = possible_courses.get(course_code);
				if (student.mayCourse(possible_course)) {
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
	private int calculateCredits(Carreer carreer, Student student) {
		if (student.isFreshMan()) {
			return carreer.creditsAmount(1, 1);
		}
		return new Random().nextInt((MAX_CREDITS - MIN_CREDITS) + 1)
				+ MIN_CREDITS;
	}
}
