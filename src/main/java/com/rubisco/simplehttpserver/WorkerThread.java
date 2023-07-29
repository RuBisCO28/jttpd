package com.rubisco.simplehttpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class WorkerThread  extends Thread {
    private final Socket socket;
    private final RequestParser requestParser;
    private final RequestHandler requestHandler;

    public WorkerThread(Socket socket, RequestParser requestParser, RequestHandler requestHandler) {
        this.socket = socket;
        this.requestParser = requestParser;
        this.requestHandler = requestHandler;
    }

    public void run() {
        try (Socket s = socket;
             InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {
            final var request = requestParser.fromInputStream(in);
            final var response = requestHandler.handleRequest(request);
            out.write(response.toBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
