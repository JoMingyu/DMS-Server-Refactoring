package com.dms.restful.dormitory.faq;

import com.dms.support.account.AdminManager;
import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.MySQL;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;


@API(functionCategory = "FAQ", summary = "업로드")
@REST(requestBody = "title : String, content : String", successCode = 201, failureCode = 204, etc = "권한이 없을 경우 fail")
@Route(uri = "/faq", method = HttpMethod.POST)
public class UploadFaq implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		if(!AdminManager.isAdmin(ctx)) {
			ctx.response().setStatusCode(204).end();
			ctx.response().close();
		}
		
		String title = ctx.request().getFormAttribute("title");
		String content = ctx.request().getFormAttribute("content");
		
		MySQL.executeUpdate("INSERT INTO faq(title, content) VALUES(?, ?)", title, content);
		
		ctx.response().setStatusCode(201).end();
		ctx.response().close();
	}
}
