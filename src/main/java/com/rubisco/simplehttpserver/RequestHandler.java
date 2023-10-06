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
    private static final String INDEX_FILE = "index.html";
    private static final String HTML_DIR_NAME = "html";

    private static final MimeDetector mimeDetector = new MimeDetector("mime.properties");

    public Response handleRequest(Request request) throws IOException {
        if (Objects.isNull(request)) {
            return new Response(Status.BAD_REQUEST, HTML_MIME, Files.readAllBytes(BAD_REQUEST_HTML_PATH));
        }

        final var normalizedRequestPath = normalizeRequestPath(request.path);

        if (isDirectoryTraversal(normalizedRequestPath)) {
            return new Response(Status.FORBIDDEN, HTML_MIME, Files.readAllBytes(FORBIDDEN_HTML_PATH));
        }

        if (isRequestForFile(normalizedRequestPath)) {
            final var mime = mimeDetector.fromFileName(request.path);
            return new Response(Status.OK, mime, Files.readAllBytes(normalizedRequestPath));
        }

        final var indexFilePath = indexFilePath(normalizedRequestPath);
        if (Files.exists(indexFilePath)) {
            return new Response(Status.OK, HTML_MIME, Files.readAllBytes(indexFilePath));
        }

        return new Response(Status.NOT_FOUND, HTML_MIME, Files.readAllBytes(NOT_FOUND_HTML_PATH));
    }

    private Path normalizeRequestPath(String requestPath) {
        return Paths.get(HTML_DIR_NAME, requestPath).normalize();
    }

    private boolean isDirectoryTraversal(Path normalizedRequestPath) {
        return !normalizedRequestPath.startsWith(HTML_DIR_NAME + "/");
    }

    private boolean isRequestForFile(Path normalizedRequestPath) {
        return Files.isRegularFile(normalizedRequestPath);
    }

    public Path indexFilePath(Path normalizedRequestPath) {
        return normalizedRequestPath.resolve(INDEX_FILE);
    }
}
