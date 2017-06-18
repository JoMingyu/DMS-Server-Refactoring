package com.dms.support.parser;

import java.util.List;

import org.hyunjun.school.SchoolException;
import org.hyunjun.school.SchoolSchedule;

import com.dms.support.util.MySQL;

public class ScheduleParser extends Parser {
	public String parse(int year, int month, int date) {
		try {
			List<SchoolSchedule> schedule = api.getMonthlySchedule(year, month);
			return schedule.get(date - 1).schedule;
			// Because zero-based numbering
		} catch (SchoolException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insertToDB(String schedule, int year, int month, int date) {
		String today = String.format("%04d-%02d-%02d", year, month, date);
		
		MySQL.executeUpdate("DELETE FROM schedule WHERE date=?", today);
		MySQL.executeUpdate("INSERT INTO schedule(date, data) VALUES(?, ?)", today, schedule);
	}
}
