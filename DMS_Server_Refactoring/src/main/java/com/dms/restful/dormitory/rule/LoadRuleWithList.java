package com.dms.restful.dormitory.rule;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.MySQL;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "기숙사 규정", summary = "리스트와 함께 내용 조회")
@REST(responseBody = "no : int, title : String, content : String, (JSONArray)", successCode = 200, failureCode = 204, etc = "조회할 faq가 없는 경우 fail")
@Route(uri = "/rule/list", method = HttpMethod.GET)
public class LoadRuleWithList implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		ResultSet rs = MySQL.executeQuery("SELECT * FROM rule");
		JSONArray response = new JSONArray();
		
		try {
			while(rs.next()) {
				JSONObject faq = new JSONObject();
				
				faq.put("no", rs.getInt("no"));
				faq.put("title", rs.getString("title"));
				faq.put("content", rs.getString("content"));
				
				response.put(faq);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(response.length() == 0) {
			ctx.response().setStatusCode(204).end();
			ctx.response().close();
		} else {
			ctx.response().setStatusCode(200);
			ctx.response().end(response.toString());
			ctx.response().close();
		}
	}
}
