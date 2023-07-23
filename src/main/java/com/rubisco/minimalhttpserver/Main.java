package com.rubisco.minimalhttpserver;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Server is running.");
        try (
                ServerSocket server = new ServerSocket(8080);
                Socket socket = server.accept();
                InputStream in = socket.getInputStream();
            ) {
            HttpRequest request = new HttpRequest(in);

            System.out.println(request.getHeaderText());
            System.out.println(request.getBodyText());
        }
        System.out.println("Server is closed.");
    }
}
