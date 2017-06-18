package com.dms.support.parser;

import java.util.List;

import org.hyunjun.school.SchoolException;
import org.hyunjun.school.SchoolMenu;
import org.json.JSONArray;

import com.dms.support.util.MySQL;

public class MealParser extends Parser {
	public SchoolMenu parse(int year, int month, int date) {
		try {
			List<SchoolMenu> menu = api.getMonthlyMenu(year, month);
			return menu.get(date - 1);
			// Because zero-based numbering
		} catch (SchoolException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insertToDB(SchoolMenu dayMenu, int year, int month, int date) {
		String[] meals = new String[3];
		// breakfast, lunch, dinner
		
		JSONArray[] array = new JSONArray[3];
		// array to insert database
		
		meals[0] = dayMenu.breakfast;
		meals[1] = dayMenu.lunch;
		meals[2] = dayMenu.dinner;
		
		for(int i = 0; i <= 2; i++) {
			array[i] = new JSONArray();
			// Instancing
			
			for(String time : meals[i].split("\n")) {
				// Each time
				
				String menu = time.split("\\*")[0];
				// Only menu, not allergy info
				
				array[i].put(menu);
			}
		}
		
		String today = String.format("%04d-%02d-%02d", year, month, date);
		
		MySQL.executeUpdate("DELETE FROM meal WHERE date=?", today);
		MySQL.executeUpdate("INSERT INTO meal(date, breakfast, lunch, dinner) VALUES(?, ?, ?, ?)", today, array[0].toString(), array[1].toString(), array[2].toString());
	}
}
