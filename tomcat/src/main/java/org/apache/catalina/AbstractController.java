package org.apache.catalina;

import org.apache.coyote.http11.request.Http11Request;
import org.apache.coyote.http11.request.HttpMethod;
import org.apache.coyote.http11.response.Http11Response;

public abstract class AbstractController implements Controller {

    @Override
    public void service(Http11Request request, Http11Response response) throws Exception {
        HttpMethod method = request.getHttpMethod();
        if (method.equals(HttpMethod.GET)) {
            doGet(request, response);
        } else if (method.equals(HttpMethod.POST)) {
            doPost(request, response);
        }
    }

    protected void doGet(Http11Request request, Http11Response response) throws Exception {
        throw new UnsupportedOperationException("GET method not implemented");
    }

    protected void doPost(Http11Request request, Http11Response response) throws Exception {
        throw new UnsupportedOperationException("POST method not implemented");
    }
}
