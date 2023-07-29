package com.rubisco.simplehttpserver;

public class Response {
    final Status status;
    final String contentType;
    final byte[] body;

    public Response(Status status, String contentType, byte[] body) {
        this.status = status;
        this.contentType = contentType;
        this.body = body;
    }
}
