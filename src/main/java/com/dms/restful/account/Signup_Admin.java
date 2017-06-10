package com.dms.restful.account;

import java.util.HashMap;

import com.dms.support.account.AdminManager;
import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "계정", summary = "회원가입 - 관리자")
@REST(requestBody = "name : String, id : String, password : String, password_confirm : String", successCode = 201, failureCode = 204, etc = "status message 존재")
@Route(uri = "/account/signup/admin", method = HttpMethod.POST)
public class Signup_Admin implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		AdminManager adminManager = new AdminManager();
		
		String id = ctx.request().getFormAttribute("id");
		String password = ctx.request().getFormAttribute("password");
		String passwordConfirm = ctx.request().getFormAttribute("password_confirm");
		String name = ctx.request().getFormAttribute("name");
		
		if(!password.equals(passwordConfirm)) {
			ctx.response().setStatusCode(204).end();
			ctx.response().close();
		} else {
			HashMap<String, Object> result = adminManager.signUp(id, password, name);
			boolean success = Boolean.parseBoolean(result.get("success").toString());
			
			if(success) {
				ctx.response().setStatusCode(201);
			} else {
				ctx.response().setStatusCode(204);
			}
			
			ctx.response().setStatusMessage(result.get("msg").toString()).end();
			ctx.response().close();
		}
	}
}
