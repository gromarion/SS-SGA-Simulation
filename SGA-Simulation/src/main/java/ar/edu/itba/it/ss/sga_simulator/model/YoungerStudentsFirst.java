package ar.edu.itba.it.ss.sga_simulator.model;

import java.util.Comparator;

public class YoungerStudentsFirst implements Comparator<Student> {
	@Override
	public int compare(Student s1, Student s2) {
		return -1*(s1.id() - s2.id());
	}
}
