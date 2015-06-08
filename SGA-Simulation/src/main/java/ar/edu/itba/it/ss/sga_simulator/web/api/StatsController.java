package ar.edu.itba.it.ss.sga_simulator.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.itba.it.ss.sga_simulator.service.StatsService;
import ar.edu.itba.it.ss.sga_simulator.web.beans.StatsMessage;

@RestController
public class StatsController {

	@Autowired
	private StatsService statsService;
	
	@RequestMapping(value = "/stats", method = RequestMethod.GET)
	public StatsMessage stats() {
		StatsMessage stats = new StatsMessage();
		stats.setTotalStudents(statsService.totalStudents());
		stats.setAlumnsMatriculated(statsService.matriculatedStudents());
		stats.setAlumnsNotMatriculated(statsService.notMatriculatedStudents());
		stats.setDayOfWeek(statsService.day());
		stats.setHourOfDay(statsService.daytime());
		stats.setCurrentlyMatriculating(statsService.studentsCurrentlyMatriculating());
		stats.setTotalsServerTimeouts(statsService.timeouts());
		return stats;
	}
}
