package com.rubisco.simplehttpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class RequestParser {
    private static final Pattern requestLinePattern = Pattern.compile("(?<method>.*) (?<path>.*?) (?<version>.*?)");
    public Request fromInputStream(InputStream in) throws IOException {
        final var reader = new BufferedReader(new InputStreamReader(in));
        final var requestLine = reader.readLine();

        if (requestLine == null) {
            return null;
        }

        var matcher = requestLinePattern.matcher(requestLine);

        if(!matcher.matches()) {
            return null;
        }

        final var method = matcher.group("method");
        final var targetPath = matcher.group("path");
        final var httpVersion = matcher.group("version");

        return new Request(method, targetPath, httpVersion);
    }
}
