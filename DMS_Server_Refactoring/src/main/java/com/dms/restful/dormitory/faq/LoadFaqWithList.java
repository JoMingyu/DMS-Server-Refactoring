package com.dms.restful.dormitory.faq;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dms.support.account.AdminManager;
import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.MySQL;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "FAQ", summary = "리스트와 함께 내용 조회")
@REST(successCode = 200, failureCode = 204, etc = "권한이 없을 경우 fail")
@Route(uri = "/faq/list", method = HttpMethod.GET)
public class LoadFaqWithList implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		if(!AdminManager.isAdmin(ctx)) {
			ctx.response().setStatusCode(204).end();
			ctx.response().close();
		}
		
		ResultSet rs = MySQL.executeQuery("SELECT * FROM faq");
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
		
		ctx.response().setStatusCode(200);
		ctx.response().end(response.toString());
		ctx.response().close();
	}
}
