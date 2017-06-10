package com.dms.support.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import io.vertx.ext.web.RoutingContext;

public class UserManager {
	private static ResultSet rs;
	
	private boolean idExists(String id) {
		rs = MySQL.executeQuery("SELECT COUNT(*) FROM account WHERE id=?", id);
		try {
			if(rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
	}
	
	public HashMap<String, Object> signUp(String uid, String id, String password) {
		uid = SHA256.encrypt(uid);
		id = AES256.encrypt(id);
		password = SHA256.encrypt(password);
		
		HashMap<String, Object> result = new HashMap<String, Object>(1);
		
		rs = MySQL.executeQuery("SELECT id FROM account WHERE uid=?", uid);
		try {
			if(rs.next()) {
				if(rs.getString("id") == null) {
					if(idExists(id)) {
						result.put("success", false);
						result.put("msg", "이미 존재하는 아이디입니다.");
					} else {
						MySQL.executeUpdate("UPDATE account SET id=?, password=? WHERE uid=?", id, password, uid);
						result.put("success", true);
						result.put("msg", "회원가입에 성공했습니다.");
					}
				} else {
					result.put("success", false);
					result.put("msg", "이미 회원가입되어 있습니다.");
				}
			} else {
				result.put("success", false);
				result.put("msg", "고유번호를 확인해 주세요.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "서버 오류입니다.");
		}
		
		return result;
	}
	
	private String getSessionFromId(String id) {
		rs = MySQL.executeQuery("SELECT * FROM account WHERE id=?", AES256.encrypt(id));
		try {
			rs.next();
			if(rs.getString("session_id") != null) {
				return rs.getString("session_id");
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
