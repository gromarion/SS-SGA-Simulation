package ar.edu.itba.it.ss.sga_simulator.model;

import java.util.HashMap;
import java.util.Map;

public class Schedule {

	// El Cronograma (Schedule) de cada materia se representara como un Mapa de
	// String ("Lu", "Ma", etc...), Integer (800, 1830, que representa los
	// horarios 8 AM y 18:30 respectivamente).
	private Map<String, Timetable> _schedule;

	public Schedule() {
		_schedule = new HashMap<String, Timetable>();
	}

	public void addSchedule(String day, int beginning, int ending)
			throws IllegalArgumentException {
		_schedule.put(day, new Timetable(beginning, ending));
	}

	public int duration() {
		int duration = 0;
		for (String day : _schedule.keySet()) {
			Timetable timetable = _schedule.get(day);
			duration += timetable.ending() - timetable.beginning();
		}
		return duration;
	}

	public boolean overlaps(Schedule schedule) {
		for (String week_day : _schedule.keySet()) {
			Timetable self_day_timetable = _schedule.get(week_day);
			Timetable other_day_timetable = schedule._schedule.get(week_day);
			return other_day_timetable != null
					&& (self_day_timetable.beginning() == other_day_timetable
							.beginning()
							|| self_day_timetable.ending() == other_day_timetable
									.ending()
							|| (self_day_timetable.beginning() < other_day_timetable
									.ending() && self_day_timetable.beginning() > other_day_timetable
									.beginning()) || (self_day_timetable
							.ending() > other_day_timetable.beginning() && self_day_timetable
							.ending() < other_day_timetable.ending()));
		}
		return false;
	}
}
