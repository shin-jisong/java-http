package org.apache.coyote.http11.response;

import java.util.Arrays;
import java.util.Set;

public enum ContentType {

    TEXT_HTML_UTF8("text/html", "charset=utf-8", "html"),
    TEXT_CSS("text/css", null, "css"),
    TEXT_JAVASCRIPT("text/javascript", null, "js")
    ;

    private final String mediaType;
    private final String parameter;
    private final String extension;

    ContentType(String mediaType, String parameter, String extension) {
        this.mediaType = mediaType;
        this.parameter = parameter;
        this.extension = extension;
    }

    public static ContentType fromExtension(String extension) {
        return Arrays.stream(values())
                .filter(contentType -> contentType.extension.equalsIgnoreCase(extension))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown extension: " + extension));
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getParameter() {
        return parameter;
    }

    public String getExtension() {
        return extension;
    }
}
