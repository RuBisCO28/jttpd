package com.rubisco.minimalhttpserver;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Server is running.");
        MinimalHttpServer server = new MinimalHttpServer();
        server.start();
        System.out.println("Server is closed.");
    }
}
