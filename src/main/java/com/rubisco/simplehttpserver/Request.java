package com.rubisco.simplehttpserver;

public class Request {
    final String method;
    final String path;
    final String version;

    public Request(String method, String path, String version) {
        this.method = method;
        this.path = path;
        this.version = version;
    }
}
