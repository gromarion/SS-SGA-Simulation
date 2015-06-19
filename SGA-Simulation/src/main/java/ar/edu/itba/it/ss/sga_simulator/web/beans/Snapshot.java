package ar.edu.itba.it.ss.sga_simulator.web.beans;

import java.util.HashMap;
import java.util.Map;

public class Snapshot {
	
	private Map<Integer, Integer> unmatriculatedAlumnsByPendingCourses = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> matriculatedAlumnsByPeningCourses = new HashMap<Integer, Integer>();
	private int totalStudents;
	private int alumnsMatriculated;
	private int alumnsNotMatriculated;
	private int alumnsMatriculatedWithErrors;
	private int dayOfWeek;
	private int hourOfDay;
	private int currentlyMatriculating = 0;
	private int totalsServerTimeouts = 0;
	
	public Snapshot() { }

	public Map<Integer, Integer> getUnmatriculatedAlumnsByPendingCourses() {
		return unmatriculatedAlumnsByPendingCourses;
	}

	public void setUnmatriculatedAlumnsByPendingCourses(
			Map<Integer, Integer> unmatriculatedAlumnsByPendingCourses) {
		this.unmatriculatedAlumnsByPendingCourses = unmatriculatedAlumnsByPendingCourses;
	}

	public Map<Integer, Integer> getMatriculatedAlumnsByPeningCourses() {
		return matriculatedAlumnsByPeningCourses;
	}

	public void setMatriculatedAlumnsByPeningCourses(
			Map<Integer, Integer> matriculatedAlumnsByPeningCourses) {
		this.matriculatedAlumnsByPeningCourses = matriculatedAlumnsByPeningCourses;
	}

	public int getCurrentlyMatriculating() {
		return currentlyMatriculating;
	}

	public void setCurrentlyMatriculating(int currentlyMatriculating) {
		this.currentlyMatriculating = currentlyMatriculating;
	}

	public int getTotalsServerTimeouts() {
		return totalsServerTimeouts;
	}

	public void setTotalsServerTimeouts(int totalsServerTimeouts) {
		this.totalsServerTimeouts = totalsServerTimeouts;
	}

	public int getTotalStudents() {
		return totalStudents;
	}

	public void setTotalStudents(int totalStudents) {
		this.totalStudents = totalStudents;
	}

	public int getHourOfDay() {
		return hourOfDay;
	}

	public void setHourOfDay(int hourOfDay) {
		this.hourOfDay = hourOfDay;
	}

	public int getAlumnsMatriculated() {
		return alumnsMatriculated;
	}

	public void setAlumnsMatriculated(int alumnsMatriculated) {
		this.alumnsMatriculated = alumnsMatriculated;
	}

	public int getAlumnsNotMatriculated() {
		return alumnsNotMatriculated;
	}

	public void setAlumnsNotMatriculated(int alumnsNotMatriculated) {
		this.alumnsNotMatriculated = alumnsNotMatriculated;
	}

	public int getAlumnsMatriculatedWithErrors() {
		return alumnsMatriculatedWithErrors;
	}

	public void setAlumnsMatriculatedWithErrors(int alumnsMatriculatedWithErrors) {
		this.alumnsMatriculatedWithErrors = alumnsMatriculatedWithErrors;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
}
