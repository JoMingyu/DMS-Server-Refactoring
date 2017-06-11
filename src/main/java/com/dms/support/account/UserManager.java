package com.dms.support.account;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.dms.support.util.MySQL;
import com.dms.support.util.SessionUtil;

import io.vertx.ext.web.RoutingContext;

public class UserManager {
	private static ResultSet rs;
	
	public void logout(RoutingContext ctx) {
		MySQL.executeUpdate("UPDATE account SET session_id=null WHERE uid=?", getEncryptedUidFromSession(ctx));
		SessionUtil.removeCookieOrSession(ctx, "UserSession");
	}
	
	public static String getEncryptedUidFromSession(RoutingContext ctx) {
		String sessionId = SessionUtil.getSessionId(ctx, "UserSession");
		
		rs = MySQL.executeQuery("SELECT uid FROM account WHERE session_id=?", sessionId);
		try {
			if(rs.next()) {
				return rs.getString("uid");
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean isLogined(RoutingContext ctx) {
		return SessionUtil.getSessionId(ctx, "UserSession") == null ? false : true;
	}
}
