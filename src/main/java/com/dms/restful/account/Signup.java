package com.dms.restful.account;

import java.util.HashMap;

import com.dms.support.account.UserManager;
import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "계정", summary = "회원가입 - 학생")
@REST(requestBody = "uid : String, id : String, password : String", successCode = 201, failureCode = 204, etc = "status message 존재")
@Route(uri = "/account/signup/student", method = HttpMethod.POST)
public class Signup implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		UserManager userManager = new UserManager();
		
		String uid = ctx.request().getFormAttribute("uid");
		String id = ctx.request().getFormAttribute("id");
		String password = ctx.request().getFormAttribute("password");
		
		HashMap<String, Object> result = userManager.signUp(uid, id, password);
		
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
