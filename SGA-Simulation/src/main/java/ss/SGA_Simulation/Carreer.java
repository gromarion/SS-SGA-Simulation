package ss.SGA_Simulation;

import java.util.Map;

public class Carreer {

	private String _name;
	//Map<YEAR, Map<COURSE_CODE, COURSE>>
	private Map<Integer, Map<Integer, Course>> _plan;
	
	public Carreer(String name, Map<Integer, Map<Integer, Course>> plan) {
		_name = name;
		_plan = plan;
	}
	
	public Map<Integer, Course> courses(int year) {
		return _plan.get(year);
	}
	
	public String name() {
		return _name;
	}
	
	public int years() {
		return _plan.keySet().size();
	}
}
