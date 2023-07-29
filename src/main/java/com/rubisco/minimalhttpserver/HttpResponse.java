package com.rubisco.minimalhttpserver;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpResponse {
    private final Status status;
    private Map<String, String> headers = new HashMap<>();
    private String body;
    private File bodyFile;

    public HttpResponse(Status status) {
        Objects.requireNonNull(status);
        this.status = status;
    }

    public void addHeader(String key, Object value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        this.headers.put(key, value.toString());
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void writeTo(OutputStream out) throws IOException {
        IOUtil.println(out, "HTTP/1.1 " + this.status);

        this.headers.forEach((k, v) -> {
            IOUtil.println(out, k + ": " + v);
        });

        if(this.body != null) {
            IOUtil.println(out, "");
            IOUtil.print(out, this.body);
        } else if (this.bodyFile != null) {
            IOUtil.println(out, "");
            Files.copy(this.bodyFile.toPath(), out);
        }
    }

    public void setBody(File file) {
        Objects.requireNonNull(file);
        this.bodyFile = file;

        String fileName = this.bodyFile.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        // TODO: use ContentType
        if ("html".equals(extension) || "htm".equals(extension)) {
            this.addHeader("Content-Type", "text/html");
        } else if ("css".equals(extension)) {
            this.addHeader("Content-Type", "text/css");
        } else if ("js".equals(extension)) {
            this.addHeader("Content-Type", "application/javascript");
        } else if ("jpg".equals(extension) || "jpeg".equals(extension)) {
            this.addHeader("Content-Type", "image/jpeg");
        } else if ("png".equals(extension)) {
            this.addHeader("Content-Type", "image/png");
        } else {
            this.addHeader("Content-Type", "text/plain");
        }
    }
}
