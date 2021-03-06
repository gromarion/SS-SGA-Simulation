package ar.edu.itba.it.ss.sga_simulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Student {

	private List<Integer> _passed_courses;
	private int _passed_credits;
	private List<Course> _desired_courses;
	private List<Integer> _matriculated_courses;
	private List<Integer> _not_matriculated_courses;
	private int _repeated_courses_amount;
	private int _current_year;
	private int _current_quarter;
	private int _id;
	private long _queue_time;
	private List<Action> _actions;
	private int _consecutive_timeouts;
	private static final int CLICKS_PER_ACTION = 4;
	public static final int MAX_CONSECUTIVE_TIMEOUTS = 5;

	public static StudentFactory getFactory() {
		return StudentFactory.STUDENT_FACTORY;
	}

	public Student(int id) {
		this(new ArrayList<Course>(), 1, 1, id);
	}
	
	public void timeout() {
		_consecutive_timeouts++;
	}
	
	public void resetTimeouts() {
		_consecutive_timeouts = 0;
	}
	
	public int consecutiveTimeouts() {
		return _consecutive_timeouts;
	}

	public Student(List<Course> desired_courses, int current_year,
			int current_quarter, int id) {
		_desired_courses = desired_courses;
		_passed_courses = new ArrayList<Integer>();
		_matriculated_courses = new ArrayList<Integer>();
		_not_matriculated_courses = new ArrayList<Integer>();
		_actions = new ArrayList<Action>();
		_current_year = current_year;
		_current_quarter = current_quarter;
		_id = id;
		prepareActionList();
	}
	
	public List<Action> actionList() {
		return _actions;
	}

	public boolean hasFinishedMatriculating() {
		return _actions.size() == 0;
	}

	public float satisfactionLevel() {
		if (_desired_courses.size() == 0) {
			return 1.0f;
		}
		return ((float) _desired_courses.size()) / _matriculated_courses.size();
	}

	public boolean isSatisfied() {
		return _matriculated_courses.size() == _desired_courses.size();
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
		} else {
			_repeated_courses_amount++;
		}
		return student_passed_course;
	}

	public int repeatedCoursesAmount() {
		return _repeated_courses_amount;
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

	public void addDesiredCourses(List<Course> courses) {
		_desired_courses = courses;
		prepareActionList();
	}

	public Course getADesiredCourse() {
		for (Course course : _desired_courses) {
			if (!_matriculated_courses.contains(course.code())
					&& !_not_matriculated_courses.contains(course.code())) {
				return course;
			}
		}
		return null;
	}
	
	public void consumeAction() {
		_actions.remove(0);
	}

	public boolean mayCourse(Course course) {
		boolean passed_correlatives = true;
		for (int correlative : course.correlatives()) {
			passed_correlatives &= _passed_courses.contains(correlative);
			if (passed_correlatives == false) {
				return false;
			}
		}
		return !passedCourse(course.code()) && passed_correlatives;
	}

	public boolean canCourse(Course course) {
		boolean passed_correlatives = true;
		for (int correlative : course.correlatives()) {
			passed_correlatives &= _passed_courses.contains(correlative);
			if (passed_correlatives == false) {
				return false;
			}
		}
		return !passedCourse(course.code())
				&& course.isBeingDictatedThisQuarter() && passed_correlatives
				&& !overlaps(course);
	}

	private boolean overlaps(Course course) {
		for (Course desired_course : _desired_courses) {
			if (desired_course.overlaps(course)) {
				return true;
			}
		}
		return false;
	}

	public String toString() {
		String ans = "Legajo: \t\t\t" + _id + "\nAño: \t\t\t\t" + _current_year
				+ "\nCuatrimestre: \t\t\t" + _current_quarter
				+ "\nMaterias aprobadas: " + _passed_courses.size() + "\t\t";
		for (Integer passed_course : _passed_courses) {
			ans += passed_course + "\n\t\t\t\t";
		}
		ans += "\nMaterias deseadas: " + _desired_courses.size() + "\t\t";
		for (Course desired_course : _desired_courses) {
			ans += desired_course.code() + "\n\t\t\t\t";
		}
		ans += "\nMaterias matriculadas: " + _matriculated_courses.size()
				+ "\t";
		for (Integer matriculated_course : _matriculated_courses) {
			ans += matriculated_course + "\n\t\t\t\t";
		}
		ans += "\nMaterias no matriculadas: "
				+ _not_matriculated_courses.size() + "\t";
		for (Integer not_matriculated_course : _not_matriculated_courses) {
			ans += not_matriculated_course + "\n\t\t\t\t";
		}
		return ans;

	}

	public void addMatriculatedCourse(Course course) {
		_matriculated_courses.add(course.code());
	}

	public void addNotMatriculatedCourse(Course course) {
		_not_matriculated_courses.add(course.code());
	}

	public void setQueueTime(long queue_time) {
		_queue_time = queue_time;
	}

	public long queueTime() {
		return _queue_time;
	}
	
	private void prepareActionList() {
		for (Course course : _desired_courses) {
			for (int i = 0; i < CLICKS_PER_ACTION; i++) {
				_actions.add(new Action());		
			}
			_actions.add(new Action(course));
		}
	}

	public Action getAction() {
		if (_actions.size() > 0) {
			return _actions.get(0); 
		}
		return null;
	}
}
