package ss.SGA_Simulation;

import java.util.concurrent.atomic.AtomicInteger;

public class Stats {

	private static Stats _instance;
	private AtomicInteger _total_students;
	private AtomicInteger _matriculated_students;
	private AtomicInteger _not_matriculated_students;
	private AtomicInteger _timeouts;
	private long _duration;
	private long _start;
	private int _daytime;
	private int _day;
	private static final int MILLIS_IN_AN_HOUR = 3600000;
	public static final int MILLIS_IN_A_MINUTE = 60000;
	private int _speed;
	
	public static Stats getInstance() {
		if (_instance == null) {
			_instance = new Stats();
		}
		return _instance;
	}
	
	private Stats() {
	}
	
	public void initialize(int speed) {
		_total_students = new AtomicInteger();
		_matriculated_students = new AtomicInteger();
		_not_matriculated_students = new AtomicInteger();
		_timeouts = new AtomicInteger();
		_speed = speed;
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
	
	public void start() {
		_start = System.currentTimeMillis();
	}
	
	public void updateDaytime(int hours_in_a_day) {
		int current_daytime = (int) ((elapsedTime(_start)) / MILLIS_IN_AN_HOUR)
				% hours_in_a_day;
		if (current_daytime != _daytime) {
			_day = (int) ((elapsedTime(_start)) / MILLIS_IN_AN_HOUR) / 24;
			_daytime = current_daytime;
		}
	}
	
	public int daytime() {
		return _daytime;
	}
	
	public int day() {
		return _day;
	}
	
	public int speed() {
		return _speed;
	}
	
	private long elapsedTime(long start) {
		return (System.currentTimeMillis() - start) * _speed;
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
