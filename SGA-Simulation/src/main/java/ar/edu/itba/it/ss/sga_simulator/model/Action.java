package ar.edu.itba.it.ss.sga_simulator.model;

public class Action {
	
	private Course _course;
	
	public Action(Course course) {
		_course = course;
	}
	
	public Action() {
	}
	
	public boolean isClickAction() {
		return _course == null;
	}
	
	public Course course() {
		return _course;
	}
}
