package com.dms.restful.dormitory.common;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.dms.support.account.UserManager;
import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.MySQL;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "시설고장신고", summary = "시설고장신고")
@REST(requestBody = "title : String, content : String, room : int", successCode = 201)
@Route(uri = "/report_faility", method = HttpMethod.POST)
public class ReportFacility implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		String uid = UserManager.getEncryptedUidFromSession(ctx);
		String title = ctx.request().getFormAttribute("title");
		String content = ctx.request().getFormAttribute("content");
		int room = Integer.parseInt(ctx.request().getFormAttribute("room"));
		String writer = null;
		
		ResultSet rs = MySQL.executeQuery("SELECT name FROM account WHERE uid=?", uid);
		try {
			if(rs.next()) {
				writer = rs.getString("name");
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		MySQL.executeUpdate("INSERT INTO facility_report(title, content, room, write_date, writer VALUES(?, ?, ?, NOW(), ?)", title, content, room, writer);
		
		ctx.response().setStatusCode(201).end();
		ctx.response().close();
	}
}
