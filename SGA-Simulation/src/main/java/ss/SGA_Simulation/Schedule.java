package ss.SGA_Simulation;

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

	public Schedule(Map<String, Timetable> schedule) {
		_schedule = schedule;
	}

	public boolean overlaps(Schedule schedule) {
		for (String week_day : _schedule.keySet()) {
			Timetable self_day_timetable = _schedule.get(week_day);
			Timetable other_day_timetable = schedule._schedule.get(week_day);
			if (other_day_timetable != null
					&& overlapsBeginnings(self_day_timetable,
							other_day_timetable)
					&& overlapsEndings(self_day_timetable, other_day_timetable)) {
				return true;
			}
		}
		return false;
	}

	private boolean overlapsBeginnings(Timetable self_day_timetable,
			Timetable other_day_timetable) {
		return self_day_timetable.beginning() <= other_day_timetable
				.beginning()
				|| self_day_timetable.beginning() >= other_day_timetable
						.beginning();
	}

	private boolean overlapsEndings(Timetable self_day_timetable,
			Timetable other_day_timetable) {
		return self_day_timetable.ending() <= other_day_timetable.ending()
				|| self_day_timetable.ending() >= other_day_timetable.ending();
	}
}
