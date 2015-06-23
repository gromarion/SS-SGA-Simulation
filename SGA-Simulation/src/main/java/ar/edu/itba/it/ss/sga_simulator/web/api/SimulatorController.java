package ar.edu.itba.it.ss.sga_simulator.web.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.itba.it.ss.sga_simulator.model.Carreer;
import ar.edu.itba.it.ss.sga_simulator.model.Server;
import ar.edu.itba.it.ss.sga_simulator.model.ConfigurationService;
import ar.edu.itba.it.ss.sga_simulator.model.Student;
import ar.edu.itba.it.ss.sga_simulator.model.StudentsQueue;
import ar.edu.itba.it.ss.sga_simulator.service.CarrerService;
import ar.edu.itba.it.ss.sga_simulator.service.MatriculationService;
import ar.edu.itba.it.ss.sga_simulator.service.StatsService;
import ar.edu.itba.it.ss.sga_simulator.service.StudentService;

@RestController
public class SimulatorController {

	@Autowired
	private MatriculationService _matriculationService;
	@Autowired
	private StatsService _statsService;
	@Autowired
	private CarrerService _carrersService;
	@Autowired
	private StudentService _studentService;
	@Autowired
	private ConfigurationService _configService;
	
	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public void start() {
		Carreer carreer = _carrersService.fetch("SoftwareEngineering.xml");
		List<Student> students = _studentService.createForCarreer(carreer);
		_matriculationService.prepareDesiredCourses(carreer, students);
		List<List<Student>> divided_students = null;
		if (_configService.divideStudentsInGroups()) {
			 divided_students = divideStudentsByCriteria(
					students, _configService.criteria(), _configService.matriculationDays());			
		} else {
			divided_students = new ArrayList<List<Student>>();
			divided_students.add(students);
		}
		_statsService.totalStudents(students.size());
		StudentsQueue queue = StudentsQueue.getInstance();
		queue.initialize("QueueConfiguration.xml", divided_students,
				_configService.matriculationDays(), _configService.divideStudentsInGroups());
		new Server(_statsService, "ServerConfiguration.xml", _configService.speed()).start();
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
