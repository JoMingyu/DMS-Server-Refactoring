package com.dms.restful.school;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.MySQL;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "학사일정", summary = "날짜별 학사일정 정보 조회")
@REST(params = "year : int, month : int, day : int", responseBody = "data : String", successCode = 200, failureCode = 204, etc = "일정이 없을 경우 fail")
@Route(uri = "/meal", method = HttpMethod.GET)
public class Schedule implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		String year = ctx.request().getFormAttribute("year");
		String month = ctx.request().getFormAttribute("month");
		String day = ctx.request().getFormAttribute("day");
		String date = String.format("%04d-%04d-%04d", year, month, day);
		
		ResultSet rs = MySQL.executeQuery("SELECT * FROM schedule WHERE date=?", date);
		try {
			rs.next();
			if(rs.getString("data").isEmpty()) {
				ctx.response().setStatusCode(204).end();
				ctx.response().close();
			} else {
				ctx.response().setStatusCode(200);
				ctx.response().end(rs.getString("data"));
				ctx.response().close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
