package com.rubisco.minimalhttpserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MinimalHttpServer {
    private ExecutorService service = Executors.newCachedThreadPool();

    public void start() {
        try (ServerSocket server = new ServerSocket(8080)) {
            while (true) {
                this.serverProcess(server);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private void serverProcess(ServerSocket server) throws IOException {
        Socket socket = server.accept();

        this.service.execute(() -> {
            try (InputStream in = socket.getInputStream();
                 OutputStream output = socket.getOutputStream();) {
                HttpRequest request = new HttpRequest(in);
                HttpHeader header = request.getHeader();
                if (header.isGetMethod()) {
                    File file = new File("./html", header.getPath());

                    if (file.exists() && file.isFile()) {
                        this.respondLocalFile(file, output);
                    } else {
                        this.respondNotFoundError(output);
                    }
                } else {
                    this.respondOk(output);
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        });
    }

    private void respondLocalFile(File file, OutputStream output) throws IOException {
        HttpResponse response = new HttpResponse(Status.OK);
        response.setBody(file);
        response.writeTo(output);
    }

    private void respondOk(OutputStream output) throws IOException {
        HttpResponse response = new HttpResponse(Status.OK);
        response.writeTo(output);
    }

    private void respondNotFoundError(OutputStream output) throws IOException {
        HttpResponse response = new HttpResponse(Status.NOT_FOUND);
        response.addHeader("Content-Type", ContentType.TEXT_PLAIN);
        response.setBody("404 Not Found");
        response.writeTo(output);
    }
}
