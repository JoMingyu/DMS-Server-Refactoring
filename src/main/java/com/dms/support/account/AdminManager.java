package com.dms.support.account;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.dms.support.util.MySQL;
import com.dms.support.util.SessionUtil;

import io.vertx.ext.web.RoutingContext;

public class AdminManager {
	private static ResultSet rs;
	
	public static String getEncryptedIdFromSession(RoutingContext ctx) {
		String sessionId = SessionUtil.getSessionId(ctx, "AdminSession");
		
		rs = MySQL.executeQuery("SELECT id FROM admin_account WHERE session_id=?", sessionId);
		try {
			if(rs.next()) {
				return rs.getString("id");
			} else {
				return null;
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean isAdmin(RoutingContext ctx) {
		return SessionUtil.getSessionId(ctx, "AdminSession") == null ? false : true;
	}
}
