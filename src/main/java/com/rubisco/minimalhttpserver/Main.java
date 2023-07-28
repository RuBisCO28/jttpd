package com.rubisco.minimalhttpserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Server is running.");
        try (ServerSocket server = new ServerSocket(8080)) {
            Socket socket = server.accept();
            InputStream in = socket.getInputStream();
            HttpRequest request = new HttpRequest(in);
            System.out.println(request.getHeaderText());
            System.out.println(request.getBodyText());

            OutputStream output = socket.getOutputStream();

            HttpResponse response = new HttpResponse(Status.OK);
            response.addHeader("Content-Type", ContentType.TEXT_HTML);
            response.setBody("<html><body><h1>Hello, World!</h1></body></html>");

            response.writeTo(output);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Server is closed.");
    }
}
