package com.dms.support.account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.dms.support.util.AES256;
import com.dms.support.util.MySQL;
import com.dms.support.util.SHA256;
import com.dms.support.util.SessionUtil;

import io.vertx.ext.web.RoutingContext;

public class AdminManager {
	private static ResultSet rs;
	
	private String getSessionFromId(String id) {
		rs = MySQL.executeQuery("SELECT * FROM admin_account WHERE id=?", id);
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
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String createSessionId() {
		String uuid;
		
		while(true) {
			uuid = SHA256.encrypt(UUID.randomUUID().toString());
			rs = MySQL.executeQuery("SELECT * FROM admin_account WHERE session_id=?", uuid);
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
			SessionUtil.createCookie(ctx, "AdminSession", sessionId);
		} else {
			SessionUtil.createSession(ctx, "AdminSession", sessionId);
		}
	}
	
	public boolean signIn(String id, String password) {
		id = AES256.encrypt(id);
		password = SHA256.encrypt(password);
		
		rs = MySQL.executeQuery("SELECT COUNT(*) FROM admin_account WHERE id=? AND password=?", id, password);
        try {
        	if(rs.next()) {
        		return true;
        	} else {
        		return false;
        	}
        } catch(SQLException e) {
        	e.printStackTrace();
        	return false;
        }
	}
	
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
