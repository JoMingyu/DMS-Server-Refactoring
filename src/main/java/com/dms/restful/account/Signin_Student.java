package com.dms.restful.account;

import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;

import io.vertx.core.http.HttpMethod;

@API(functionCategory = "계정", summary = "로그인 - 학생")
@REST(requestBody = "", successCode = 0, failureCode = 0, etc = "")
@Route(uri = "/account/signin/admin", method = HttpMethod.POST)
public class Signin_Student {

}
