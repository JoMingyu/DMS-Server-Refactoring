package com.dms.restful.account;

import com.dms.support.account.UserManager;
import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.AES256;
import com.dms.support.util.MySQL;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "계정", summary = "학번 교체")
@REST(requestBody = "number : String(4자리)", successCode = 201, failureCode = 204, etc = "4자릿수 학번이 아닐 경우 fail")
@Route(uri = "/change_number/student", method = HttpMethod.POST)
public class ChangeStudentNumber implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		String uid = UserManager.getEncryptedUidFromSession(ctx);
		String number = AES256.encrypt(ctx.request().getFormAttribute("number"));
		
		if(number.length() != 4) {
			ctx.response().setStatusCode(204).end();
			ctx.response().close();
		}
		
		MySQL.executeUpdate("UPDATE account SET number=? WHERE uid=?", number, uid);
		
		ctx.response().setStatusCode(201).end();
		ctx.response().close();
	}
}
