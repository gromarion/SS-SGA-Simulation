package ar.edu.itba.it.ss.sga_simulator.model;

import java.util.List;

public class Course {

	private String _name;
	private int _room;
	private int _enrolled_students;
	private int _code;
	private int _credits;
	private double _repeating_course_probability;
	private Schedule _schedule;
	private List<Integer> _correlatives;
	private boolean _is_final_project;

	public Course(String name, int room, int code, int credits,
			double repeating_course_probability, List<Integer> correlatives,
			boolean isFinalProject, Schedule schedule)
			throws IllegalArgumentException {
		_name = name;
		_room = room;
		_code = code;
		_credits = credits;
		_repeating_course_probability = repeating_course_probability;
		_correlatives = correlatives;
		_is_final_project = isFinalProject;
		int duration = schedule.duration();
		if (duration == 0 || duration == _credits) {
			_schedule = schedule;
		} else {
			throw new IllegalArgumentException(
					"Credits amount must be equal to toal week duration of the course");
		}
	}

	public String name() {
		return _name;
	}

	public int room() {
		return _room;
	}

	public List<Integer> correlatives() {
		return _correlatives;
	}

	public double repeatingCourseProbability() {
		return _repeating_course_probability;
	}

	public int code() {
		return _code;
	}

	public int credits() {
		return _credits;
	}

	public boolean overlaps(Course course) {
		return _schedule.overlaps(course._schedule);
	}

	public String toString() {
		String course = "Nombre:\t\t\t\t" + _name + "\nCodigo:\t\t\t\t" + _code
				+ "\nCreditos:\t\t\t" + _credits + "\nCupo:\t\t\t\t" + _room
				+ "\nAlumnos: \t\t\t" + _enrolled_students
				+ "\nProbabilidad de Recursar: \t"
				+ _repeating_course_probability + "\nEs proyecto final: \t\t"
				+ _is_final_project + "\nCorrelativas:\t\t\t";
		for (Integer correlative : _correlatives) {
			course += correlative + "\n\t\t\t\t";
		}
		return course + "\n";
	}

	public boolean isDictatingThisQuarter() {
		if (_schedule.duration() == 0) {
			return _is_final_project;
		}
		return true;
	}
}
