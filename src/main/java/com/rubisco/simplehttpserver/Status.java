package com.rubisco.simplehttpserver;

public enum Status {
    OK("200 OK"),
    BAD_REQUEST("400 Bad Request"),
    FORBIDDEN("403 Forbidden"),
    NOT_FOUND("404 Not Found"),
    INTERNAL_SERVER_ERROR("500 Internal Server Error");

    private final String statusCode;

    Status(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getCode() {
        return statusCode;
    }
}
