package ss.SGA_Simulation;

import java.util.HashMap;
import java.util.Map;

public class Year {

	private int _year;
	private Map<Integer, Map<Integer, Course>> _quarters;
	
	public Year(int year) {
		_year = year;
		_quarters = new HashMap<Integer, Map<Integer, Course>>();
	}
	
	public Map<Integer, Course> quarterCourses(int quarter) {
		return _quarters.get(quarter);
	}
	
	public void addQuarterPlan(int quarter, Map<Integer, Course> quarter_plan) {
		_quarters.put(quarter, quarter_plan);
	}
	
	public Map<Integer, Map<Integer, Course>> quarters() {
		return _quarters;
	}
	
	public int year() {
		return _year;
	}
}
