package ss.SGA_Simulation;

import java.util.List;

public class Course {

	private String _name;
	private int _room;
	private int _enrolled_students;
	private int _year;
	private int _code;
	private double _repeating_course_probability;
	private List<Integer> _correlatives;

	public Course(String name, int room, int year, int code,
			double repeating_course_probability, List<Integer> correlatives) {
		_name = name;
		_room = room;
		_year = year;
		_code = code;
		_repeating_course_probability = repeating_course_probability;
		_correlatives = correlatives;
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

	public String toString() {
		String course = "Nombre:\t\t\t" + _name + "\nCodigo:\t\t\t" + _code
				+ "\nCupo:\t\t\t" + _room + "\nAlumnos: \t\t"
				+ _enrolled_students + "\nCorrelativas:\t\t";
		for (Integer correlative : _correlatives) {
			course += correlative + "\n\t\t\t";
		}
		return course;
	}
}
