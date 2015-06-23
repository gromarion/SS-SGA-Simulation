package ar.edu.itba.it.ss.sga_simulator.model;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.it.ss.sga_simulator.service.MatriculationService;

public enum StudentFactory {

	STUDENT_FACTORY;

	public MatriculationService matriculationService;

	public List<Student> create(Carreer carreer) {
		if (carreer.studentsAmount() % carreer.years() != 0) {
			throw new IllegalArgumentException(
					"Students amount must be a multiple of carreer years");
		}
		System.out.println("Creando alumnos...");
		List<Student> students = new ArrayList<Student>();
		for (int i = 0; i < carreer.studentsAmount(); i++) {
			students.add(new Student(i));
			if (i % 10 == 0) {
				System.out.print(".");
			}
		}
		System.out.println("\n" + carreer.studentsAmount()
				+ " alumnos creados.");
		return students;
	}
}
