package ar.edu.itba.it.ss.sga_simulator.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.it.ss.sga_simulator.model.Carreer;
import ar.edu.itba.it.ss.sga_simulator.model.Course;
import ar.edu.itba.it.ss.sga_simulator.model.Student;
import ar.edu.itba.it.ss.sga_simulator.model.StudentFactory;
import ar.edu.itba.it.ss.sga_simulator.model.StudentRepository;

@Service
public class StudentService {

	@Autowired
	private MatriculationService matriculationService;
	@Autowired
	private StudentRepository _students;
	
	public List<Student> createForCarreer(Carreer carreer) {
		List<Student> students = StudentFactory.STUDENT_FACTORY.create(carreer);
		_students.saveAll(students);
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

	public Map<Integer, Integer> getUnmatriculatedAlumnsByPendingCourses() {
		// TODO Auto-generated method stub
		Map<Integer, Integer> unmatriculatedAlumnsByPendingCourses = new HashMap<Integer, Integer>();
		for (Student s : _students.fetchAll()) {
			if (!s.hasFinishedMatriculating()) {
				Integer count = unmatriculatedAlumnsByPendingCourses.get(s.passedCredits());
				if (count == null) {
					count = 0;
				}
				unmatriculatedAlumnsByPendingCourses.put(s.passedCredits(), count + 1); 
			}
		}
		return unmatriculatedAlumnsByPendingCourses;
	}

	public Map<Integer, Integer> getMatriculatedAlumnsByPendingCourses() {
		Map<Integer, Integer> matriculatedAlumnsByPendingCourses = new HashMap<Integer, Integer>();
		for (Student s : _students.fetchAll()) {
			if (s.hasFinishedMatriculating()) {
				Integer count = matriculatedAlumnsByPendingCourses.get(s.passedCredits());
				if (count == null) {
					count = 0;
				}
				matriculatedAlumnsByPendingCourses.put(s.passedCredits(), count + 1); 
			}
		}
		return matriculatedAlumnsByPendingCourses;
	}

}
