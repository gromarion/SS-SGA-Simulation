package ss.SGA_Simulation;

import java.util.concurrent.atomic.AtomicInteger;

public class Stats {

	private static Stats _instance;
	private AtomicInteger _total_students;
	private AtomicInteger _matriculated_students;
	private AtomicInteger _not_matriculated_students;
	private AtomicInteger _timeouts;
	private long _duration;
	
	public static Stats getInstance() {
		if (_instance == null) {
			_instance = new Stats();
		}
		return _instance;
	}
	
	private Stats() {
		_total_students = new AtomicInteger();
		_matriculated_students = new AtomicInteger();
		_not_matriculated_students = new AtomicInteger();
		_timeouts = new AtomicInteger();
	}
	
	public void setTotalStudents(int total_students) {
		_total_students.set(total_students);
	}
	
	public void addMatriculatedStudent() {
		_matriculated_students.incrementAndGet();
	}
	
	public void addNotMatriculatedStudent() {
		_not_matriculated_students.incrementAndGet();
	}
	
	public int totalStudents() {
		return _total_students.intValue();
	}
	
	public int matriculatedStudents() {
		return _matriculated_students.intValue();
	}
	
	public int notMatriculatedStudents() {
		return _not_matriculated_students.intValue();
	}
	
	public int timeouts() {
		return _timeouts.get();
	}
	
	public void addTimeout() {
		_timeouts.incrementAndGet();
	}
	
	public void setDuration(long millis) {
		_duration = millis;
	}
	
	public long millis() {
		return _duration;
	}
	
	public long seconds() {
		return millis() / 1000;
	}
	
	public long minutes() {
		return seconds() / 60;
	}
	
	public int hours() {
		return (int) minutes() / 60;
	}
	
	public int days() {
		return hours() / 24;
	}
	
	public void printDuration() {
		String ans = "D: " + days();
		int hours = hours() % 24;
		ans += " H: " + hours;
		int minutes = (int) minutes() % 60;
		ans += " M: " + minutes;
		int seconds = (int) seconds() % 60;
		ans += " S: " + seconds;
		System.out.println(ans);
	}
}
