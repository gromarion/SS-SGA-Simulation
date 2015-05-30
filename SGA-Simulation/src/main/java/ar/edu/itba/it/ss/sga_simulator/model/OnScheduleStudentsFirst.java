package ar.edu.itba.it.ss.sga_simulator.model;

import java.util.Comparator;

public class OnScheduleStudentsFirst implements Comparator<Student> {
	@Override
	public int compare(Student s1, Student s2) {
		return s1.repeatedCoursesAmount() - s2.repeatedCoursesAmount();
	}
}
