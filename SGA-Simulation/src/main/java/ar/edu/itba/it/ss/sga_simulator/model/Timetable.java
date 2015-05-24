package ar.edu.itba.it.ss.sga_simulator.model;

public class Timetable {

	private int _beginning;
	private int _ending;

	public Timetable(int beginning, int ending) {
		_beginning = beginning;
		_ending = ending;
	}
	
	public int durationMinutes() {
		int duration = (_beginning - _ending);
		return (duration / 100) * 60 + (duration % 100);
	}
	
	public int beginning() {
		return _beginning;
	}
	
	public int ending() {
		return _ending;
	}
}
