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

            while(line != null && !line.isEmpty()) {
                header.append(line + "\n");
                line = reader.readLine();
            }
            System.out.println(header);
        }
        System.out.println("Server is closed.");
    }
}
