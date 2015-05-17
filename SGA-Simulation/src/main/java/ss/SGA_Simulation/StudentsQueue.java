package ss.SGA_Simulation;

import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

public class StudentsQueue extends Thread {

	private Queue<Student> _queue;
	private List<Student> _students;
	
	public StudentsQueue(List<Student> students) {
		_queue = new PriorityBlockingQueue<Student>(students.size());
		_students = students;
	}
	
	public void run() {
		while(!finished()) {
			double probability = new Random().nextDouble();
			if (_students.size() > 1 && probability > 0.5) {
				int random_student_index = new Random().nextInt(_students.size() - 1);
				Student student = _students.remove(random_student_index);
				add(student);
			}			
		}
	}
	
	public boolean isEmpty() {
		return _queue.isEmpty();
	}
	
	public boolean finished() {
		return _queue.size() == 0 && _students.size() == 0;
	}
	
	public Student poll() {
		return _queue.poll();
	}

	public boolean add(Student student) {
		student.setQueueTime(System.currentTimeMillis());
		return _queue.add(student);
	}
}
