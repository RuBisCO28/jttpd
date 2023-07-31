package com.rubisco.simplehttpserver;

import java.io.IOException;
import java.util.Properties;

public class MimeDetector {
    private final Properties prop;

    public MimeDetector(String configFileName) {
        final var in = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileName);
        final var props = new Properties();
        try {
            props.load(in);
        } catch (IOException e) {
           System.err.println("Failed to load mime type config file: " + e.getMessage());
        }
        this.prop = props;
    }

    /**
     * Response MIME supported files extension
     * If not found, return "application/octet-stream"
     */
    public String fromFileName(String fileName) {
        final var ext = fileName.substring(fileName.indexOf(".") + 1);
        return this.prop.getProperty(ext, "application/octet-stream");
    }
}
