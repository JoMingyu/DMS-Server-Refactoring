package com.dms.restful.dormitory.common;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import com.dms.support.account.UserManager;
import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.MySQL;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "사용자 정보", summary = "상벌점 상태 조회")
@REST(responseBody = "merit : int, demerit : int", successCode = 201)
@Route(uri = "/score", method = HttpMethod.GET)
public class Score implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		String uid = UserManager.getEncryptedUidFromSession(ctx);
		
		ResultSet rs = MySQL.executeQuery("SELECT * FROM student_score WHERE uid=?", uid);
		
		JSONObject response = new JSONObject();
		try {
			if(rs.next()) {
				response.put("merit", rs.getInt("merit"));
				response.put("demerit", rs.getInt("demerit"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		ctx.response().setStatusCode(200);
		ctx.response().end(response.toString());
		ctx.response().close();
	}
}
