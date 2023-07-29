package com.rubisco.simplehttpserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class RequestHandler {
    private static final Path BAD_REQUEST_HTML_PATH = Paths.get("html/400.html");
    private static final Path FORBIDDEN_HTML_PATH = Paths.get("html/403.html");
    private static final Path NOT_FOUND_HTML_PATH = Paths.get("html/404.html");
    private static final String HTML_MIME = "text/html;charset=utf8";
    public Response handleRequest(Request request) throws IOException {

        if (Objects.isNull(request)) {
            return new Response(Status.BAD_REQUEST, HTML_MIME, Files.readAllBytes(BAD_REQUEST_HTML_PATH));
        }

        return new Response(Status.NOT_FOUND, HTML_MIME, Files.readAllBytes(NOT_FOUND_HTML_PATH));
    }
}
