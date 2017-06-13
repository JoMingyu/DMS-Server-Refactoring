package com.dms.restful.dormitory.common;

import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@API(functionCategory = "시설고장신고", summary = "시설고장신고")
@REST()
@Route(uri = "/report_faility", method = HttpMethod.POST)
public class ReportFacility implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		
	}
}
