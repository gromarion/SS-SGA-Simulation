package ar.edu.itba.it.ss.sga_simulator.web.beans;

public class ConfigMessage {

	private boolean divideStudentsInGroups;
	private String criteria;
	private boolean logEnabled;
	private int matriculationDays;
	private int speed;

	public String getCriteria() {
		return criteria;
	}
	
	public void setCriteria(String criteriaName) {	
		this.criteria = criteriaName;		
	}

	public boolean getLogEnabled() {
		return logEnabled;
	}
	
	public void setLogEnabled(boolean logEnabled) {
		this.logEnabled = logEnabled;		
	}

	public int getMatriculationDays() {
		return matriculationDays;
	}
	
	public void setMatriculationDays(int matriculationDays) {
		this.matriculationDays = matriculationDays;		
	}

	public int getSpeed() {
		return speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean getDivideStudentsInGroups() {
		return divideStudentsInGroups;
	}

	public void setDivideStudentsInGroups(boolean divideStudentsInGroups) {
		this.divideStudentsInGroups = divideStudentsInGroups;
	}

}
