package com.rubisco.simplehttpserver;

import java.io.IOException;
import java.net.ServerSocket;

public class SimpleHttpServer {
    private static final int PORT = 8001;

    public static void main(String[] args) throws IOException {
        final var serverSocket = new ServerSocket(PORT);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Shutting down HTTP Server...");
                serverSocket.close();
            } catch (IOException e) {
                System.exit(1);
            }
        }));

        System.out.println("HTTP Server Start! Listening at " + PORT + "!");

        while (true) {
            try {
                var socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Failed to dispatch: " + e.getMessage());
            }
        }
    }
}
