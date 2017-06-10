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

public class AdminManager {
	private static ResultSet rs;
	
	private boolean idExists(String id) {
		rs = MySQL.executeQuery("SELECT COUNT(*) FROM admin_account WHERE id=?", id);
		try {
			if(rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return true;
		}
	}
	
	public HashMap<String, Object> signUp(String id, String password, String name) {
		id = AES256.encrypt(id);
		password = SHA256.encrypt(password);
		name = AES256.encrypt(name);
		
		HashMap<String, Object> result = new HashMap<String, Object>(1);
		
		if(idExists(id)) {
			result.put("success", false);
			result.put("msg", "이미 존재하는 아이디입니다.");
		} else {
			MySQL.executeUpdate("INSERT INTO admin_account (id, password, name) VALUES(?, ?, ?)", id, password, name);
			result.put("success", true);
			result.put("msg", "회원가입에 성공했습니다.");
		}
		
		return result;
	}
	
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
