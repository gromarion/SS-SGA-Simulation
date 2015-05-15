package ss.SGA_Simulation;

import java.util.ArrayList;
import java.util.List;

public class Student {

	private List<Integer> _passed_courses;
	private List<Integer> _desired_courses;
	private List<Integer> _matriculated_courses;
	private List<Integer> _not_matriculated_courses;
	private int _current_year;
	private int _id;

	public Student(List<Integer> desired_courses, int current_year, int id) {
		_desired_courses = desired_courses;
		_passed_courses = new ArrayList<Integer>();
		_matriculated_courses = new ArrayList<Integer>();
		_not_matriculated_courses = new ArrayList<Integer>();
		_current_year = current_year;
		_id = id;
	}

	public boolean hasFinishedMatriculating() {
		return _matriculated_courses.size() + _not_matriculated_courses.size() == _desired_courses
				.size();
	}
}
