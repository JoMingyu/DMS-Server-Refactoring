package com.dms.restful.account;

import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "계정", summary = "회원가입 - 관리자")
@REST(requestBody = "uid : String, id : String, password : String", successCode = 201, failureCode = 204, etc = "status message 존재")
@Route(uri = "/account/signup/admin", method = HttpMethod.POST)
public class Signup_Admin implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		
	}
}
