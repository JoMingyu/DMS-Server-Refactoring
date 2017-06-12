package com.dms.restful.account;

import com.dms.support.account.UserManager;
import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.MySQL;
import com.dms.support.util.SessionUtil;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "계정", summary = "로그아웃 - 관리자")
@REST(successCode = 201, failureCode = 204, etc = "쿠키나 세션 삭제")
@Route(uri = "/logout/admin", method = HttpMethod.POST)
public class Logout_Admin implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		MySQL.executeUpdate("UPDATE account SET session_id=null WHERE uid=?", UserManager.getEncryptedUidFromSession(ctx));
		SessionUtil.removeCookieOrSession(ctx, "UserSession");
	}
}
