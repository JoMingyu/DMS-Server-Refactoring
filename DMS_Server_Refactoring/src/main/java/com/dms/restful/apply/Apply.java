package com.dms.restful.apply;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import com.dms.support.account.UserManager;
import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;
import com.dms.support.util.MySQL;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "신청", summary = "신청(키워드 기반)")
@REST(requestBody = "keyword : String, (extension), no : int, seat : int, (goingout), sat : boolean, sun : boolean, (stay), value : int, (merit), content : String, target : String(optional)", successCode = 201, failureCode = 204, etc = "신청 시간이 아닐 경우 fail")
@Route(uri = "/apply", method = HttpMethod.POST)
public class Apply implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		String uid = UserManager.getEncryptedUidFromSession(ctx);
		String keyword = ctx.request().getFormAttribute("keyword");
		
		switch(keyword) {
		case "extension":
			if(!canApplyExtension()) {
				ctx.response().setStatusCode(204).end();
				ctx.response().close();
			}
			
			// Can extension apply
			int no = Integer.parseInt(ctx.request().getFormAttribute("no"));
			int seat = Integer.parseInt(ctx.request().getFormAttribute("seat"));

			MySQL.executeUpdate("DELETE FROM extension_apply WHERE uid=?", uid);
			ResultSet rs = MySQL.executeQuery("SELECT name FROM student_data WHERE uid=?", uid);
			try {
				rs.next();
				String name = rs.getString("name");
				MySQL.executeUpdate("INSERT INTO extension_apply(no, seat, name, uid) VALUES(?, ?, ?, ?)", no, seat, name, uid);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			break;
		case "goingout":
			boolean sat = Boolean.parseBoolean(ctx.request().getFormAttribute("sat"));
			boolean sun = Boolean.parseBoolean(ctx.request().getFormAttribute("sun"));
			
			MySQL.executeUpdate("DELETE FROM goingout_apply WHERE uid=?", uid);
			MySQL.executeUpdate("INSERT INTO goingout_apply(uid, sat, sun) VALUES(?, ?, ?)", uid, sat, sun);
			
			break;
		case "stay":
			if(!canApplyStay()) {
				ctx.response().setStatusCode(204).end();
				ctx.response().close();
			}
			
			// Can stay apply
			int value = Integer.parseInt(ctx.request().getFormAttribute("value"));
			
			MySQL.executeUpdate("DELETE FROM stay_apply WHERE uid=?", uid);
			MySQL.executeUpdate("INSERT INTO stay_apply(uid, value) VALUES(?, ?))", uid, value);
			
			break;
		case "merit":
			String content = ctx.request().getFormAttribute("content");
			
			if(ctx.request().formAttributes().contains("target")) {
				String target = ctx.request().getFormAttribute("target");
				MySQL.executeUpdate("INSERT INTO merit_apply(uid, target, content) VALUES(?, ?, ?)", uid, target, content);
			} else {
				MySQL.executeUpdate("INSERT INTO merit_apply(uid, content) VALUES(?, ?)", uid, content);
			}
			
			break;
		default:
			ctx.response().setStatusCode(204).end();
			ctx.response().close();
			return;
		}
		
		ctx.response().setStatusCode(201).end();
		ctx.response().close();
	}
	
	private boolean canApplyExtension() {
		Calendar current = Calendar.getInstance();
        int hour = current.get(Calendar.HOUR_OF_DAY);
        int minute = current.get(Calendar.MINUTE);

        if((hour > 17 && hour < 20) || (hour == 17 && minute >= 30) || (hour == 20 && minute < 30)) {
        	return true;
        } else {
        	return false;
        }
	}
	
	private boolean canApplyStay() {
        Calendar current = Calendar.getInstance();
        int today = current.get(Calendar.DAY_OF_WEEK);
        int currentHour = current.get(Calendar.HOUR_OF_DAY);
        int currentMinute = current.get(Calendar.MINUTE);
        
        if(Calendar.SUNDAY <= today && today >= Calendar.WEDNESDAY) {
        	// Between Monday and Wednesday
        	return true;
        } else if(Calendar.THURSDAY == today) {
        	if(currentHour < 20 || (currentHour ==20 && currentMinute <= 30)) {
        		return true;
        	} else {
        		return false;
        	}
        } else {
        	return false;
        }
	}
}
