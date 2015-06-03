package ar.edu.itba.it.ss.sga_simulator.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
			System.out.println(i);
		}
		System.out.println("\n" + carreer.studentsAmount()
				+ " alumnos creados.");
		return simulateCarreer(carreer, students);
	}

	private List<Student> simulateCarreer(Carreer carreer,
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

	private void simulateQuarter(Carreer carreer, Student student) {
		List<Course> courses = matriculationService.fetchCourses(carreer,
				student);
		for (Course course : courses) {
			student.course(course);
		}
		student.promote(carreer);
	}

	private void setMatriculationService(
			MatriculationService matriculationService) {
		this.matriculationService = matriculationService;
	}

	@Component
	public static class StudentFactoryInjector {

		@Autowired
		public MatriculationService matriculationService;

		@PostConstruct
		public void postContruct() {
			StudentFactory.STUDENT_FACTORY
					.setMatriculationService(matriculationService);
		}
	}
}
