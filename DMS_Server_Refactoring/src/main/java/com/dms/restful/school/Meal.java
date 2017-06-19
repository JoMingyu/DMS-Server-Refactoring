package com.dms.restful.school;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.MySQL;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "급식", summary = "날짜별 급식 정보 조회")
@REST(params = "year : int, month : int, day : int", responseBody = "breakfast : JSONArray, lunch : JSONArray, dinner : JSONArray", successCode = 200, failureCode = 204, etc = "급식이 없을 경우 fail")
@Route(uri = "/meal", method = HttpMethod.GET)
public class Meal implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		String year = ctx.request().getFormAttribute("year");
		String month = ctx.request().getFormAttribute("month");
		String day = ctx.request().getFormAttribute("day");
		String date = String.format("%04d-%04d-%04d", year, month, day);
		
		ResultSet rs = MySQL.executeQuery("SELECT * FROM meal WHERE date=?", date);
		try {
			if(rs.next()) {
				JSONObject response = new JSONObject();
				
				response.put("breakfast", rs.getString("breakfast"));
				response.put("lunch", rs.getString("lunch"));
				response.put("dinner", rs.getString("dinner"));
				
				ctx.response().setStatusCode(200).end();
				ctx.response().close();
			} else {
				ctx.response().setStatusCode(204).end();
				ctx.response().close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
