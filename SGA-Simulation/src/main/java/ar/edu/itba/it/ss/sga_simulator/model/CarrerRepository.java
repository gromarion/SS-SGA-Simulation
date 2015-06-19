package ar.edu.itba.it.ss.sga_simulator.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class CarrerRepository {

	private Map<String, Carreer> _carrers = new HashMap<String, Carreer>();
	
	public Carreer fetch(String string) {
		if (!_carrers.containsKey(string)) {
			_carrers.put(string, CarreerParser.parse(string));
		}
		return _carrers.get(string);
	}

}
