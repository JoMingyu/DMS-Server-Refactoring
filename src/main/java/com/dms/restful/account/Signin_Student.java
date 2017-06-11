package com.dms.restful.account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.AES256;
import com.dms.support.util.MySQL;
import com.dms.support.util.SHA256;
import com.dms.support.util.SessionUtil;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "계정", summary = "로그인 - 학생")
@REST(requestBody = "id : String, password : String, keep_login : boolean", successCode = 201, failureCode = 204)
@Route(uri = "/account/signin/student", method = HttpMethod.POST)
public class Signin_Student implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		String id = AES256.encrypt(ctx.request().getFormAttribute("id"));
		String password = SHA256.encrypt(ctx.request().getFormAttribute("password"));
		boolean keepLogin = Boolean.parseBoolean(ctx.request().getFormAttribute("keep_login"));
		
		ResultSet rs = MySQL.executeQuery("SELECT COUNT(*) FROM account WHERE id=? AND password=?", id, password);
		try {
			if(rs.next()) {
				String sessionId = getSessionFromId(id);
				
				if(sessionId == null) {
					sessionId = createSessionId();
				}
				
				if(keepLogin) {
					SessionUtil.createCookie(ctx, "UserSession", sessionId);
				} else {
					SessionUtil.createSession(ctx, "UserSession", sessionId);
				}
				
				MySQL.executeUpdate("UPDATE account SET session_id=? WHERE id=?", SHA256.encrypt(sessionId), id);
				
				ctx.response().setStatusCode(201).end();
				ctx.response().close();
			} else {
				ctx.response().setStatusCode(204).end();
				ctx.response().close();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String getSessionFromId(String id) {
		ResultSet rs = MySQL.executeQuery("SELECT * FROM account WHERE id=?", id);
		try {
			if(rs.next()) {
				if(rs.getString("session_id") != null) {
					return rs.getString("session_id");
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String createSessionId() {
		while(true) {
			String uuid = SHA256.encrypt(UUID.randomUUID().toString());
			ResultSet rs = MySQL.executeQuery("SELECT * FROM account WHERE session_id=?", uuid);
			try {
				if(!rs.next()) {
					return uuid;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
