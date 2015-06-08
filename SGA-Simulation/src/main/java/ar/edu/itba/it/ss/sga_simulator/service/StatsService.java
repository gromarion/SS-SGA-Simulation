package ar.edu.itba.it.ss.sga_simulator.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class StatsService {

	private int _total_students;
	private int _matriculated_students;
	private int _not_matriculated_students;
	private Map<Integer, Map<Integer, Integer>> _timeouts_per_day_per_hour;
	private int _students_currently_matriculating;
	private int _timeouts; // self explained
	private long _duration; // time since the simulation started
	private long _start; // time when the simluation started
	private int _daytime; // a moment of time during the day [0,1,2,3,....,23]
	private int _day;
	private static final int MILLIS_IN_AN_HOUR = 3600000;
	public static final int MILLIS_IN_A_MINUTE = 60000;
	public static final int HOURS_IN_A_DAY = 24;
	private int _speed;
	private int _satisfied_students_amount;
	private boolean _log_enabled;

	public StatsService() {
		_timeouts_per_day_per_hour = new HashMap<Integer, Map<Integer, Integer>>();
	}

	public int timeoutsOnDay(int day) {
		if (_timeouts_per_day_per_hour.size() < day) {
			return 0;
		}
		int ans = 0;
		for (int each_day : _timeouts_per_day_per_hour.keySet()) {
			for (int timeouts : _timeouts_per_day_per_hour.get(each_day)
					.keySet()) {
				ans += timeouts;
			}
		}
		return ans;
	}

	public int timeoutsOnDayAtTime(int day, int hour) {
		if (_timeouts_per_day_per_hour.size() < day) {
			return 0;
		} else {
			int day_hours_amount = _timeouts_per_day_per_hour.get(day).size();
			if (day_hours_amount > hour || day_hours_amount < hour) {
				return 0;
			}
		}
		return _timeouts_per_day_per_hour.get(day).get(hour);
	}

	private void addTimeout(int day, int hour) {
		if (_timeouts_per_day_per_hour.containsKey(day)) {
			addTimeoutAtDayAtHour(day, hour);
		} else {
			Map<Integer, Integer> timeouts_in_a_day = new HashMap<Integer, Integer>();
			timeouts_in_a_day.put(hour, 1);
			_timeouts_per_day_per_hour.put(day, timeouts_in_a_day);
		}
	}

	private void addTimeoutAtDayAtHour(int day, int hour) {
		Map<Integer, Integer> timeouts_in_a_day = _timeouts_per_day_per_hour
				.get(day);
		if (timeouts_in_a_day.containsKey(hour)) {
			timeouts_in_a_day.put(hour, timeouts_in_a_day.get(hour) + 1);
		} else {
			timeouts_in_a_day.put(hour, 1);
		}
	}

	public void logEnabled(boolean value) {
		_log_enabled = value;
	}

	public void speed(int speed) {
		_speed = speed;
	}

	public void totalStudents(int total_students) {
		_total_students = total_students;
	}

	public void addMatriculatedStudent() {
		_matriculated_students++;
	}

	public void addNotMatriculatedStudent() {
		_not_matriculated_students++;
	}

	public int totalStudents() {
		return _total_students;
	}

	public int matriculatedStudents() {
		return _matriculated_students;
	}

	public int notMatriculatedStudents() {
		return _not_matriculated_students;
	}

	public int timeouts() {
		return _timeouts;
	}

	public void addTimeout() {
		_timeouts++;
		addTimeout(day(), daytime());
	}

	public void duration(long millis) {
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

	public void updateDaytime() {
		int current_daytime = (int) ((elapsedTime(_start)) / MILLIS_IN_AN_HOUR)
				% HOURS_IN_A_DAY;
		if (current_daytime != _daytime) {
			_day = (int) ((elapsedTime(_start)) / MILLIS_IN_AN_HOUR) / 24;
			_daytime = current_daytime;
			log("DAY: " + dayName() + ", TIME: " + _daytime);
		}
	}

	private void log(String message) {
		if (_log_enabled) {
			System.out.println(message);
		}
	}

	public int daytime() {
		return _daytime;
	}

	public int day() {
		return _day;
	}

	public String dayName() {
		int day = day() % 7;
		switch (day) {
		case 0:
			return "mon";
		case 1:
			return "tue";
		case 2:
			return "wed";
		case 3:
			return "thu";
		case 4:
			return "fri";
		case 5:
			return "sat";
		case 6:
			return "sun";
		default:
			return null;
		}
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

	public void addStatisfiedStudent() {
		_satisfied_students_amount++;
	}

	public int satisfiedStudentsAmount() {
		return _satisfied_students_amount;
	}

	public int insatisfiedStudentsAmount() {
		return _total_students - _satisfied_students_amount;
	}
	
	public int studentsCurrentlyMatriculating() {
		return _students_currently_matriculating;
	}
	
	public int increaseStudentsCurrentlyMatriculating() {
		return ++_students_currently_matriculating;
	}
	
	public int decreaseStudentsCurrentlyMatriculating() {
		return --_students_currently_matriculating;
	}
}
