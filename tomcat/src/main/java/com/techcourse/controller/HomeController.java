package com.techcourse.controller;

import org.apache.catalina.Controller;
import org.apache.coyote.http11.request.Http11Request;
import org.apache.coyote.http11.response.ContentType;
import org.apache.coyote.http11.response.Http11Response;
import org.apache.coyote.http11.response.Http11ResponseBuilder;
import org.apache.coyote.http11.response.StatusCode;

public class HomeController implements Controller {

    @Override
    public void service(Http11Request request, Http11Response response) throws Exception {
        Http11ResponseBuilder.build(response, StatusCode.OK, ContentType.TEXT_HTML_UTF8, "Hello world!");
    }
}
