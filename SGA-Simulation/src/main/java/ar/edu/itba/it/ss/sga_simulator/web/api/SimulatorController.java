package ar.edu.itba.it.ss.sga_simulator.web.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.itba.it.ss.sga_simulator.model.Carreer;
import ar.edu.itba.it.ss.sga_simulator.model.CarreerParser;
import ar.edu.itba.it.ss.sga_simulator.model.Server;
import ar.edu.itba.it.ss.sga_simulator.model.SimulationConfiguration;
import ar.edu.itba.it.ss.sga_simulator.model.Student;
import ar.edu.itba.it.ss.sga_simulator.model.StudentsQueue;
import ar.edu.itba.it.ss.sga_simulator.service.MatriculationService;
import ar.edu.itba.it.ss.sga_simulator.service.StatsService;

@RestController
public class SimulatorController {

	@Autowired
	private MatriculationService _matriculationService;
	@Autowired
	private StatsService _stats;

	@RequestMapping("/start")
	public void start() {
		Carreer carreer = CarreerParser.parse("SoftwareEngineering.xml");
		List<Student> students = Student.getFactory().create(carreer);
		_matriculationService.prepareDesiredCourses(carreer, students);
		SimulationConfiguration sc = new SimulationConfiguration(
				"SimulationConfiguration.xml");
		List<List<Student>> divided_students = null;
		if (sc.divideStudentsInGroups()) {
			 divided_students = divideStudentsByCriteria(
					students, sc.criteria(), sc.matriculationDays());			
		} else {
			divided_students = new ArrayList<List<Student>>();
			divided_students.add(students);
		}
		_stats.speed(sc.speed());
		_stats.totalStudents(students.size());
		_stats.logEnabled(sc.logEnabled());
		StudentsQueue queue = StudentsQueue.getInstance();
		queue.initialize("QueueConfiguration.xml", divided_students,
				sc.matriculationDays(), sc.divideStudentsInGroups());
		new Server(_stats, "ServerConfiguration.xml", sc.speed()).start();
		queue.start();
	}

	private List<List<Student>> divideStudentsByCriteria(
			List<Student> students, Comparator<Student> criteria,
			int matriculation_days) {
		List<List<Student>> ans = new ArrayList<List<Student>>();
		int batch_size = students.size() / matriculation_days;
		Collections.sort(students, criteria);
		for (int i = 0; i < matriculation_days; i++) {
			List<Student> students_in_day_i = new ArrayList<Student>();
			for (int j = 0; j < batch_size; j++) {
				students_in_day_i.add(students.get(i * batch_size + j));
			}
			ans.add(students_in_day_i);
		}
		return ans;
	}
}
