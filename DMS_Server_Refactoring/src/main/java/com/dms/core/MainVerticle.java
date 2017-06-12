package com.dms.core;

import com.dms.support.routing.Routing;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class MainVerticle extends AbstractVerticle {
	@Override
	public void start() throws Exception {
		Router router = Router.router(vertx);
		
		router.route().handler(BodyHandler.create().setUploadsDirectory("upload-files"));
		router.route().handler(CookieHandler.create());
		router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
		Routing.route(router, "com.dms.restful");
		router.route().handler(StaticHandler.create());
		
		vertx.createHttpServer().requestHandler(router::accept).listen(80);
	}
}
