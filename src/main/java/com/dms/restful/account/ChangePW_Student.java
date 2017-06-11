package com.dms.restful.account;

import com.dms.support.account.UserManager;
import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.MySQL;
import com.dms.support.util.SHA256;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "계정", summary = "비밀번호 변경 - 학생")
@REST(requestBody = "new_password : String", successCode = 201)
@Route(uri = "/change_password/student", method = HttpMethod.POST)
public class ChangePW_Student implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		String uid = UserManager.getEncryptedUidFromSession(ctx);
		String newPassword = SHA256.encrypt(ctx.request().getFormAttribute("new_password"));
		
		MySQL.executeUpdate("UPDATE account SET password=? WHERE uid=?", newPassword, uid);
		
		ctx.response().setStatusCode(201).end();
		ctx.response().close();
	}
}
