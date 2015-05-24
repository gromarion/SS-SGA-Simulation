package ar.edu.itba.it.ss.sga_simulator.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.itba.it.ss.sga_simulator.service.StatsService;

@RestController
public class StatsController {

	@Autowired
	private StatsService statsService;
	
	
}
