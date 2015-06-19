package ar.edu.itba.it.ss.sga_simulator.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.itba.it.ss.sga_simulator.model.ConfigurationService;
import ar.edu.itba.it.ss.sga_simulator.web.beans.ConfigMessage;

@RestController
public class ConfigController {

	@Autowired
	private ConfigurationService _configService;
	
	@RequestMapping(value = "/config", method = RequestMethod.GET)
	public ConfigMessage config() {
		ConfigMessage cm = new ConfigMessage();
		cm.setDivideStudentsInGroups(_configService.divideStudentsInGroups());
		cm.setCriteria(_configService.criteriaName());
		cm.setLogEnabled(_configService.logEnabled());
		cm.setMatriculationDays(_configService.matriculationDays());
		cm.setSpeed(_configService.speed());
		return cm;
	}
	
	@RequestMapping(value = "/config", method = RequestMethod.POST)
	public void config(ConfigMessage cm) {
		_configService.divideStudentsInGroups(cm.getDivideStudentsInGroups());
		_configService.criteria(cm.getCriteria());
		_configService.logEnabled(cm.getLogEnabled());
		_configService.matriculationDays(cm.getMatriculationDays());
		_configService.speed(cm.getSpeed());
	}
	
}
