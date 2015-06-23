package ar.edu.itba.it.ss.sga_simulator.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

@Repository
public class StudentRepository {

	private Set<Student> _students = new HashSet<Student>();
	
	public void saveAll(List<Student> students) {
		_students.addAll(students);		
	}

	public Set<Student> fetchAll() {
		return Collections.unmodifiableSet(_students);
	}	
}
