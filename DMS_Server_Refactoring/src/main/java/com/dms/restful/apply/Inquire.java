package com.dms.restful.apply;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dms.support.account.UserManager;
import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.AES256;
import com.dms.support.util.MySQL;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "신청", summary = "신청 정보 조회(키워드 기반)")
@REST(requestBody = "keyword : String, (extension | goingout | stay)", responseBody = "(extension), no : int, seat : int, name : String, (goingout), sat : boolean, sun : boolean, (stay), value : int, (merit), content : String, has_target : boolean, target : String", successCode = 201, failureCode = 204, etc = "신청 정보가 없을 경우 fail")
@Route(uri = "/apply", method = HttpMethod.GET)
public class Inquire implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		String uid = UserManager.getEncryptedUidFromSession(ctx);
		String keyword = ctx.request().getFormAttribute("keyword");
		
		JSONObject response = new JSONObject();
		ResultSet rs;
		try {
			switch(keyword) {
			case "extension":
				rs = MySQL.executeQuery("SELECT * FROM extension_apply WHERE uid=?", uid);
			
				if(rs.next()) {
					response.put("no", rs.getInt("no"));
					response.put("seat", rs.getInt("seat"));
					response.put("name", AES256.decrypt(rs.getString("name")));
				}
			
				break;
			case "goingout":
				rs = MySQL.executeQuery("SELECT * FROM goingout_apply WHERE uid=?", uid);
				
				if(rs.next()) {
					response.put("sat", rs.getBoolean("sat"));
					response.put("sun", rs.getBoolean("sun"));
				}
				
				break;
			case "stay":
				rs = MySQL.executeQuery("SELECT * FROM stay_apply WHERE uid=?", uid);
				
				if(rs.next()) {
					response.put("value", rs.getInt("value"));
				}
				
				break;
			case "merit":
				// Uses array
				
				JSONArray responseArray = new JSONArray();
				rs = MySQL.executeQuery("SELECT * FROM merit_apply WHERE uid=?", uid);
				
				while(rs.next()) {
					response.put("no", rs.getInt("no"));
					response.put("content", rs.getString("content"));
					if(rs.getString("target").isEmpty()) {
						response.put("has_target", true);
						response.put("target", rs.getString("target"));
					} else {
						response.put("has_target", false);
					}
					
					responseArray.put(response);
				}
				
				if(responseArray.length() == 0) {
					ctx.response().setStatusCode(204).end();
					ctx.response().close();
				} else {
					ctx.response().setStatusCode(201).end(responseArray.toString());
					ctx.response().close();
				}
				
				return;
			default:
				break;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		if(response.length() == 0) {
			ctx.response().setStatusCode(204).end();
			ctx.response().close();
		} else {
			ctx.response().setStatusCode(201).end(response.toString());
			ctx.response().close();
		}
	}
}
