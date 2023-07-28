package com.rubisco.minimalhttpserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Server is running.");
        try (ServerSocket server = new ServerSocket(8080)) {
            Socket socket = server.accept();
            InputStream in = socket.getInputStream();
            HttpRequest request = new HttpRequest(in);

            HttpHeader header = request.getHeader();

            OutputStream output = socket.getOutputStream();

            if (header.isGetMethod()) {
                File file = new File(".", header.getPath());

                if (file.exists() && file.isFile()) {
                    respondLocalFile(file, output);
                } else {
                    respondNotFoundError(output);
                }
            } else {
                respondOk(output);
            }

            socket.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Server is closed.");
    }

    private static void respondLocalFile(File file, OutputStream output) throws IOException {
        HttpResponse response = new HttpResponse(Status.OK);
        response.setBody(file);
        response.writeTo(output);
    }

    private static void respondOk(OutputStream output) throws IOException {
        HttpResponse response = new HttpResponse(Status.OK);
        response.writeTo(output);
    }

    private static void respondNotFoundError(OutputStream output) throws IOException {
        HttpResponse response = new HttpResponse(Status.NOT_FOUND);
        response.addHeader("Content-Type", ContentType.TEXT_PLAIN);
        response.setBody("404 Not Found");
        response.writeTo(output);
    }
}
