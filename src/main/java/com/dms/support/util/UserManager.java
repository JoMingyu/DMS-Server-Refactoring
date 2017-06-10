package com.dms.support.util;

import io.vertx.ext.web.RoutingContext;

public class UserManager {
	public static String getEncryptedUidFromSession(RoutingContext ctx) {
		SessionUtil.getSessionId(ctx, "UserSession");
	}
}
