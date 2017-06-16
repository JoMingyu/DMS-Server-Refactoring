package com.dms.restful.dormitory.apply;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.AES256;
import com.dms.support.util.MySQL;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "신청", summary = "연장신청 맵 조회")
@REST(requestBody = "no : int()", responseBody = "no : int, name : String, map : String", successCode = 201)
@Route(uri = "/extension_map", method = HttpMethod.GET)
public class InquireExtensionMap implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		int no = Integer.parseInt(ctx.request().getFormAttribute("no"));
		
		ResultSet rs = MySQL.executeQuery("SELECT * FROM extension_map WHERE no=?", no);
		JSONObject response = new JSONObject();
		try {
			if(rs.next()) {
				response.put("no", rs.getInt("no"));
				response.put("name", rs.getString("name"));
				response.put("map", makeMap(rs.getInt("no"), rs.getString("map")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static JSONArray makeMap(int classNo, String map) {
		ResultSet rs = MySQL.executeQuery("SELECT * FROM extension_apply WHERE no=? ", classNo);
		Map<Integer, String> seatData = new HashMap<Integer, String>();
		
		try {
			while(rs.next()) {
				seatData.put(rs.getInt("seat"), rs.getString("name"));
				// Get apply status
			}
			
			JSONArray mapArray = new JSONArray(map);
			// Parse map to JSON array : [[], [], []]
			
			int seatCount = 1;
			for(int i = 0; i < mapArray.length(); i++) {
				JSONArray seatRow = mapArray.getJSONArray(i);
				// Changes are automatically applied to mapArray
				
				for(int j = 0; j < seatRow.length(); j++) {
					if(seatRow.getInt(j) == 1) {
						// Available position
						
						seatRow.put(j, seatData.containsKey(seatCount) ? seatData.get(seatCount) : seatCount);
						// Set applier's name when applied, Set seatCount when not applied

						seatCount++;
						// Accumulate seat count only available position
					}
				}
			}
			
			return mapArray;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
