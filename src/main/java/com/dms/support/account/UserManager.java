package com.dms.support.account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import com.dms.support.util.AES256;
import com.dms.support.util.MySQL;
import com.dms.support.util.SHA256;
import com.dms.support.util.SessionUtil;

import io.vertx.ext.web.RoutingContext;

public class UserManager {
	private static ResultSet rs;
	
	private String getSessionFromId(String id) {
		rs = MySQL.executeQuery("SELECT * FROM account WHERE id=?", id);
		try {
			if(rs.next()) {
				if(rs.getString("session_id") != null) {
					return rs.getString("session_id");
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String createSessionId() {
		String uuid;
		
		while(true) {
			uuid = SHA256.encrypt(UUID.randomUUID().toString());
			rs = MySQL.executeQuery("SELECT * FROM account WHERE session_id=?", uuid);
			try {
				if(!rs.next()) {
					return uuid;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void registerSessionId(RoutingContext ctx, boolean keepLogin, String id) {
		id = AES256.encrypt(id);
		
		String sessionId = getSessionFromId(id);
		
		if(sessionId == null) {
			sessionId = createSessionId();
		}
		
		if(keepLogin) {
			SessionUtil.createCookie(ctx, "UserSession", sessionId);
		} else {
			SessionUtil.createSession(ctx, "UserSession", sessionId);
		}
		
		MySQL.executeUpdate("UPDATE account SET session_id=? WHERE id=?", SHA256.encrypt(sessionId), id);
	}
	
	public boolean signIn(String id, String password) {
		id = AES256.encrypt(id);
		password = SHA256.encrypt(password);
		
		rs = MySQL.executeQuery("SELECT COUNT(*) FROM account WHERE id=? AND password=?", id, password);
		try {
			if(rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
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
