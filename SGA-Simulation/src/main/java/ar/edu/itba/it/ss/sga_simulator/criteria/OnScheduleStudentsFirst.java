package ar.edu.itba.it.ss.sga_simulator.criteria;

import java.util.Comparator;

import ar.edu.itba.it.ss.sga_simulator.model.Student;

public class OnScheduleStudentsFirst implements Comparator<Student> {
	@Override
	public int compare(Student s1, Student s2) {
		return s1.repeatedCoursesAmount() - s2.repeatedCoursesAmount();
	}
}
