package ss.SGA_Simulation;

import java.util.concurrent.atomic.AtomicInteger;

public class Stats {

	private static Stats _instance;
	private AtomicInteger _total_students;
	private AtomicInteger _matriculated_students;
	private AtomicInteger _not_matriculated_students;
	
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
}
