package com.dms.restful.dormitory.notice;

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

@API(functionCategory = "공지사항", summary = "리스트와 함께 내용 조회")
@REST(responseBody = "no : int, title : String, content : String, writer : String, (JSONArray)", successCode = 200, failureCode = 204, etc = "조회할 notice가 없는 경우 fail")
@Route(uri = "/notice/list", method = HttpMethod.GET)
public class LoadNoticeWithList implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		ResultSet rs = MySQL.executeQuery("SELECT * FROM notice");
		JSONArray response = new JSONArray();
		
		try {
			while(rs.next()) {
				JSONObject notice = new JSONObject();
				
				notice.put("no", rs.getInt("no"));
				notice.put("title", rs.getString("title"));
				notice.put("content", rs.getString("content"));
				notice.put("writer", rs.getString("writer"));
				
				response.put(notice);
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
