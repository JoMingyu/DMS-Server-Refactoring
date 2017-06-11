package com.dms.restful.account;

import com.dms.support.account.AdminManager;
import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.MySQL;
import com.dms.support.util.SessionUtil;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "계정", summary = "로그아웃 - 학생")
@REST(successCode = 201, failureCode = 204, etc = "쿠키나 세션 삭제")
@Route(uri = "/logout/student", method = HttpMethod.POST)
public class Logout_Student implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		MySQL.executeUpdate("UPDATE admin_account SET session_id=null WHERE id=?", AdminManager.getEncryptedIdFromSession(ctx));
		SessionUtil.removeCookieOrSession(ctx, "AdminSession");
	}
}
