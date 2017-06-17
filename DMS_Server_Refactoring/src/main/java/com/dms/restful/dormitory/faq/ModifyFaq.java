package com.dms.restful.dormitory.faq;

import com.dms.support.account.AdminManager;
import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.MySQL;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "FAQ", summary = "수정")
@REST(requestBody = "no : int, title : String, content : String", successCode = 200, failureCode = 204, etc = "권한이 없을 경우 fail")
@Route(uri = "/faq", method = HttpMethod.PATCH)
public class ModifyFaq implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		if(!AdminManager.isAdmin(ctx)) {
			ctx.response().setStatusCode(204).end();
			ctx.response().close();
		}
		
		int no = Integer.parseInt(ctx.request().getFormAttribute("no"));
		String title = ctx.request().getFormAttribute("title");
		String content = ctx.request().getFormAttribute("content");
		
		MySQL.executeUpdate("UPDATE faq SET title=?, content=? WHERE no=?", title, content, no);
		
		ctx.response().setStatusCode(200).end();
		ctx.response().close();
	}
}
