package org.apache.coyote.http11.response;

public enum ContentType {
    TEXT_HTML_UTF8("text/html", "charset=utf-8");

    private final String mediaType;
    private final String parameter;

    ContentType(String mediaType, String parameter) {
        this.mediaType = mediaType;
        this.parameter = parameter;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getParameter() {
        return parameter;
    }
}
