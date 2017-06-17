package com.dms.restful.dormitory.notice;

import com.dms.support.routing.API;
import com.dms.support.routing.REST;
import com.dms.support.routing.Route;

import io.vertx.core.http.HttpMethod;

@API(functionCategory = "공지사항", summary = "수정")
@REST(requestBody = "no : int, title : String, content : String, writer : String", successCode = 200, failureCode = 204, etc = "권한이 없을 경우 fail")
@Route(uri = "/notice", method = HttpMethod.PATCH)
public class ModifyNotice {

}
