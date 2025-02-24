package org.apache.coyote.http11;

import org.apache.catalina.Controller;
import org.apache.catalina.FileController;
import org.apache.coyote.Processor;
import org.apache.coyote.http11.request.Http11Request;
import org.apache.coyote.http11.request.Http11RequestBuilder;
import org.apache.coyote.http11.response.Http11Response;
import org.apache.coyote.http11.response.Http11ResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.Socket;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;
    private final RequestMapping requestMapping = new RequestMapping();

    public Http11Processor(final Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        log.info("connect host: {}, port: {}", connection.getInetAddress(), connection.getPort());
        process(connection);
    }

    @Override
    public void process(final Socket connection) {
        try (final var inputStream = connection.getInputStream();
             final var outputStream = connection.getOutputStream()) {

            Http11Request request = Http11RequestBuilder.build(inputStream);
            Http11Response response = handleRequest(request);

            outputStream.write(Http11ResponseWriter.write(response));
            outputStream.flush();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Http11Response handleRequest(Http11Request request) throws Exception {
        Controller controller = requestMapping.getController(request);
        if (controller == null) {
            controller = new FileController();
        }
        Http11Response response = new Http11Response();
        controller.service(request, response);
        return response;
    }
}
