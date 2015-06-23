package ar.edu.itba.it.ss.sga_simulator.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.itba.it.ss.sga_simulator.service.StatsService;
import ar.edu.itba.it.ss.sga_simulator.service.StudentService;
import ar.edu.itba.it.ss.sga_simulator.web.beans.Snapshot;

@RestController
public class StatsController {

	@Autowired
	private StatsService statsService;
	@Autowired
	private StudentService studentService;
	
	@RequestMapping(value = "/stats", method = RequestMethod.GET)
	public Snapshot stats() {
		Snapshot stats = new Snapshot();
		stats.setUnmatriculatedAlumnsByPendingCourses(studentService.getUnmatriculatedAlumnsByPendingCourses());
		stats.setMatriculatedAlumnsByPeningCourses(studentService.getMatriculatedAlumnsByPendingCourses());
		stats.setTotalStudents(statsService.totalStudents());
		stats.setAlumnsMatriculated(statsService.matriculatedStudents());
		stats.setAlumnsNotMatriculated(statsService.notMatriculatedStudents());
		stats.setDayOfWeek(statsService.day());
		stats.setHourOfDay(statsService.daytime());
		stats.setCurrentlyMatriculating(statsService.studentsCurrentlyMatriculating());
		stats.setTotalsServerTimeouts(statsService.timeouts());
        stats.setSatisfiedStudentsAmount(statsService.satisfiedStudentsAmount());
		return stats;
	}
}
