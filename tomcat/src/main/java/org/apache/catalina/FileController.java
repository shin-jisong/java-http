package org.apache.catalina;

import org.apache.coyote.http11.request.Http11Request;
import org.apache.coyote.http11.response.ContentType;
import org.apache.coyote.http11.response.Http11Response;
import org.apache.coyote.http11.response.Http11ResponseBuilder;
import org.apache.coyote.http11.response.StatusCode;

public class FileController implements Controller {

    @Override
    public void service(Http11Request request, Http11Response response) throws Exception {
        String fileName = request.getUri();
        int dotIndex = fileName.lastIndexOf('.');
        String extension = fileName.substring(dotIndex + 1);

        Http11ResponseBuilder.buildFile(response, StatusCode.OK, ContentType.fromExtension(extension), fileName);
    }
}
