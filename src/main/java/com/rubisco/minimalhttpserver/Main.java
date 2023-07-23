package com.rubisco.minimalhttpserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Server is running.");
        try (
            ServerSocket server = new ServerSocket(8080);
            Socket socket = server.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            ) {
            String line = reader.readLine();
            StringBuilder header = new StringBuilder();
            int contentLength = 0;

            while(line != null && !line.isEmpty()) {
                if (line.startsWith("Content-Length")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }

                header.append(line + "\n");
                line = reader.readLine();
            }

            String body = null;

            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                reader.read(buffer);
                body = new String(buffer);
            }

            System.out.println(header);
            System.out.println(body);
        }
        System.out.println("Server is closed.");
    }
}
