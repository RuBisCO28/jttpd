package com.rubisco.minimalhttpserver;

import java.io.*;
import java.util.stream.Stream;

public class HttpRequest {
    private static final String CRLF = "\r\n";
    private final String headerText;
    private final String bodyText;

    public HttpRequest(InputStream input) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"))) {
            this.headerText = this.readHeader(reader);
            this.bodyText = this.readBody(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String readHeader(BufferedReader in) throws IOException {
        String line = in.readLine();
        StringBuilder header = new StringBuilder();

        while (line != null && !line.isEmpty()) {
            header.append(line + CRLF);
            line = in.readLine();
        }
        return header.toString();
    }

    private String readBody(BufferedReader in) throws IOException {
        final int contentLength = this.getContentLength();

        if (contentLength <= 0) {
            return null;
        }

        char[] buffer = new char[contentLength];
        in.read(buffer);
        return new String(buffer);
    }

    private int getContentLength() {
        return Stream.of(this.headerText.split(CRLF))
                .filter(line -> line.startsWith("Content-Length"))
                .map(line -> line.split(":")[1].trim())
                .mapToInt(Integer::parseInt)
                .findFirst()
                .orElse(0);
    }

    public String getHeaderText() {
        return this.headerText;
    }

    public String getBodyText() {
        return this.bodyText;
    }
}
