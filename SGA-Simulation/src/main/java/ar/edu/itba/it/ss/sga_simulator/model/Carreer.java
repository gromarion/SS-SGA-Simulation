package ar.edu.itba.it.ss.sga_simulator.model;

import java.util.Map;

public class Carreer {

	private String _name;
	// Map<YEAR, Map<QUARTER, MAP<COURSE_CODE, COURSE>>>
	private Map<Integer, Year> _plan;
	private int _courses_amount;
	private static final int QUARTERS = 2;
	private int _students_amount;

	public Carreer(String name, Map<Integer, Year> plan, int students_amount) {
		_name = name;
		_plan = plan;
		calculateCoursesAmount();
		_students_amount = students_amount;
	}

	public Map<Integer, Map<Integer, Course>> courses(int year) {
		return _plan.get(year).quarters();
	}

	public String name() {
		return _name;
	}

	public int years() {
		return _plan.keySet().size();
	}
	
	public int quarters() {
		return years()*2;
	}

	public String toString() {
		String ans = _name + "\nMaterias: " + _courses_amount + "\n";
		for (int i = 1; i < years(); i++) {
			Map<Integer, Map<Integer, Course>> quarters = courses(i);
			for (Integer quarter : quarters.keySet()) {
				ans += "CUATRIMESTRE " + quarter + "\n";
				for (Integer code : quarters.get(quarter).keySet()) {
					ans += quarters.get(quarter).get(code) + "\n";
				}
			}
		}
		return ans;
	}

	public int creditsAmount(int year, int quarter) {
		int credits = 0;
		Map<Integer, Course> courses = _plan.get(year).quarterCourses(quarter);
		for (Integer course : courses.keySet()) {
			credits += courses.get(course).credits();
		}
		return credits;
	}
	
	public int coursesAmount() {
		return _courses_amount;
	}
	
	public int studentsAmount() {
		return _students_amount;
	}

	private void calculateCoursesAmount() {
		for (int year : _plan.keySet()) {
			for (int i = 1; i <= QUARTERS; i++) {
				_courses_amount += _plan.get(year).quarterCourses(i).keySet()
						.size();
			}
		}
	}
}
