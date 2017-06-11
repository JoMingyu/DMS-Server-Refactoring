package com.dms.restful.account;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.AES256;
import com.dms.support.util.MySQL;
import com.dms.support.util.SHA256;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "계정", summary = "회원가입 - 관리자")
@REST(requestBody = "name : String, id : String, password : String, password_confirm : String", successCode = 201, failureCode = 204, etc = "status message 존재")
@Route(uri = "/account/signup/admin", method = HttpMethod.POST)
public class Signup_Admin implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		String id = AES256.encrypt(ctx.request().getFormAttribute("id"));
		String password = SHA256.encrypt(ctx.request().getFormAttribute("password"));
		String passwordConfirm = SHA256.encrypt(ctx.request().getFormAttribute("password_confirm"));
		String name = AES256.encrypt(ctx.request().getFormAttribute("name"));
		
		if(!password.equals(passwordConfirm)) {
			ctx.response().setStatusCode(204).end();
			ctx.response().close();
		}
		
		String statusMessage = null;
		int statusCode = 0;
		
		if(!idExists(id)) {
			MySQL.executeUpdate("INSERT INTO admin_account(id, password, name) VALUES(?, ?, ?)", id, password, name);
			statusMessage = "회원가입에 성공했습니다.";
			statusCode = 201;
		} else {
			statusMessage = "이미 존재하는 아이디입니다.";
			statusCode = 204;
		}
		
		ctx.response().setStatusCode(statusCode);
		ctx.response().setStatusMessage(statusMessage).end();
		ctx.response().close();
	}
	
	private boolean idExists(String id) {
		ResultSet rs = MySQL.executeQuery("SELECT COUNT(*) FROM admin_account WHERE id=?", id);
		try {
			if(rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
	}
}
