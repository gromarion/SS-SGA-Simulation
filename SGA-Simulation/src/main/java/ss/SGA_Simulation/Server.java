package ss.SGA_Simulation;

import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

	private StudentsQueue _queue;
	private long _time_per_request;
	private long _max_time_per_request;
	private List<Student> _matriculated_students;

	public Server(long time_per_request, long max_time_per_request, StudentsQueue queue) {
		_time_per_request = time_per_request;
		_max_time_per_request = max_time_per_request;
		_matriculated_students = new ArrayList<Student>();
		_queue = queue;
	}

	public void run() {
		while (!_queue.finished()) {
			attendRequest();
		}
	}

	private void attendRequest() {
		if (_queue.isEmpty()) {
			System.out.println("empty");
			return;
		} else {
			Student student = _queue.poll();
			long start = System.currentTimeMillis();
			System.out.println("Attending " + student.id());
			while (System.currentTimeMillis() - start < _time_per_request)
				; // Demora un tiempo _time_per_request en atender la solicitud
					// del estudiante
			if (System.currentTimeMillis() - student.queueTime() > _max_time_per_request) {
				_queue.add(student);
				System.out.println(student.id() + " TIMEOUT");
			} else {
				attend(student);
			}
		}
	}

	private void attend(Student student) {
		Course course = student.getADesiredCourse();
		if (course != null) {
			if (student.canCourse(course)) {
				student.addMatriculatedCourse(course);
			} else {
				student.addNotMatriculatedCourse(course);
			}
			if (!student.hasFinishedMatriculating()) {
				_queue.add(student);
			} else {
				_matriculated_students.add(student);
				System.out.println("Student " + student.id()
						+ " finished matriculating!!!");
			}
		}
	}
}
