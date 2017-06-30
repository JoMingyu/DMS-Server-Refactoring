 package com.dms.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public interface LogHandler extends Handler<RoutingContext> {
	static LogHandler create() {
		return new LogHandlerImpl();
	}
}
