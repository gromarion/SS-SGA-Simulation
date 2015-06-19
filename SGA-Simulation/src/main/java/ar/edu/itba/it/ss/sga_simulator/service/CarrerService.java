package ar.edu.itba.it.ss.sga_simulator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.it.ss.sga_simulator.model.Carreer;
import ar.edu.itba.it.ss.sga_simulator.model.CarrerRepository;

@Service
public class CarrerService {

	@Autowired
	private CarrerRepository _carrers;
	
	public Carreer fetch(String string) {
		return _carrers.fetch(string);
	}

}
