package ss.SGA_Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Student {

	private List<Integer> _passed_courses;
	private int _passed_credits;
	private List<Integer> _desired_courses;
	private List<Integer> _matriculated_courses;
	private List<Integer> _not_matriculated_courses;
	private int _current_year;
	private int _current_quarter;
	private int _id;

	public Student(int id) {
		this(new ArrayList<Integer>(), 1, 1, id);
	}

	public Student(List<Integer> desired_courses, int current_year,
			int current_quarter, int id) {
		_desired_courses = desired_courses;
		_passed_courses = new ArrayList<Integer>();
		_matriculated_courses = new ArrayList<Integer>();
		_not_matriculated_courses = new ArrayList<Integer>();
		_current_year = current_year;
		_current_quarter = current_quarter;
		_id = id;
	}

	public boolean hasFinishedMatriculating() {
		return _matriculated_courses.size() + _not_matriculated_courses.size() == _desired_courses
				.size();
	}

	public int currentYear() {
		return _current_year;
	}

	public int currentQuarter() {
		return _current_quarter;
	}

	public int id() {
		return _id;
	}

	public boolean passedCourse(int course_code) {
		return _passed_courses.contains(course_code);
	}

	public int passedCoursesAmount() {
		return _passed_courses.size();
	}

	public int passedCredits() {
		return _passed_credits;
	}

	public boolean course(Course course) {
		double probability = new Random().nextDouble();
		boolean student_passed_course = probability > course
				.repeatingCourseProbability();
		if (student_passed_course) {
			_passed_courses.add(course.code());
			_passed_credits += course.credits();
		}
		return student_passed_course;
	}

	public boolean isFreshMan() {
		return _current_year == 1 && _current_quarter == 1
				&& _passed_courses.size() == 0;
	}

	public void promote(Carreer carreer) {
		boolean passed_all_courses = true;
		while (true) {
			if (_passed_courses.size() == carreer.coursesAmount()) {
				return;
			}
			for (Integer course_code : carreer.courses(_current_year)
					.get(_current_quarter).keySet()) {
				passed_all_courses &= _passed_courses.contains(course_code);
				if (!passed_all_courses) {
					return;
				}
			}
			if (_current_quarter == 2) {
				_current_year++;
				_current_quarter = 1;
			} else {
				_current_quarter++;
			}
		}
	}

	public boolean canCourse(Course course) {
		boolean passed_correlatives = true;
		for (int correlative : course.correlatives()) {
			passed_correlatives &= _passed_courses.contains(correlative);
			if (passed_correlatives == false) {
				return false;
			}
		}
		return !passedCourse(course.code()) && passed_correlatives;
	}

	public String toString() {
		String ans = "Legajo: \t\t" + _id + "\nAño: \t\t\t" + _current_year
				+ "\nCuatrimestre: \t\t" + _current_quarter
				+ "\nMaterias aprobadas: " + _passed_courses.size() + "\t";
		for (Integer passed_course : _passed_courses) {
			ans += passed_course + "\n\t\t\t";
		}
		return ans;

	}
}
