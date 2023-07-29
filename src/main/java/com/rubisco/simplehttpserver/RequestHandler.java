package com.rubisco.simplehttpserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RequestHandler {
    private static final Path NOT_FOUND_HTML_PATH = Paths.get("html/404.html");
    private static final String HTML_MIME = "text/html;charset=utf8";
    public Response handleRequest(Request request) throws IOException {
        return new Response(Status.NOT_FOUND, HTML_MIME, Files.readAllBytes(NOT_FOUND_HTML_PATH));
    }
}
