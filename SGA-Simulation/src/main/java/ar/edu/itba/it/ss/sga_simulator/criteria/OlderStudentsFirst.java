package ar.edu.itba.it.ss.sga_simulator.criteria;

import java.util.Comparator;

import ar.edu.itba.it.ss.sga_simulator.model.Student;

public class OlderStudentsFirst implements Comparator<Student> {
	@Override
	public int compare(Student s1, Student s2) {
		return s1.id() - s2.id();
	}
}
