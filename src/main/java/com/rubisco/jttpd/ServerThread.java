package com.rubisco.jttpd;

import java.io.*;
import java.net.Socket;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.ResourceBundle;

public class ServerThread implements Runnable {
    private static final String RESOURCE_NAME = "application";
    private static final String SERVER_NAME = "localhost:8001";
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        OutputStream output = null;
        ResourceBundle rb = ResourceBundle.getBundle(RESOURCE_NAME);
        final String DOCUMENT_ROOT = rb.getString("document_root");
        final String ERROR_DOCUMENT = rb.getString("error_document_root");
        try {
            InputStream input = socket.getInputStream();

            String line = "";
            String path = "";
            String ext = "";
            String host = "";
            while ((line = Util.readLine(input)) != null) {
                if (line == "")
                    break;
                if (line.startsWith("GET")) {
                    path = MyURLDecoder.decode(line.split(" ")[1], "UTF-8");
                    String[] tmp = path.split("\\.");
                    ext = tmp[tmp.length - 1];
                } else if (line.startsWith("Host:")) {
                    host = line.substring("Host: ".length());
                }
            }
            if (path == null) {
                return;
            }

            if (path.endsWith("/")) {
                path += "index.html";
                ext = "html";
            }

            output = new BufferedOutputStream(socket.getOutputStream());

            FileSystem fs = FileSystems.getDefault();
            Path pathObj = fs.getPath(DOCUMENT_ROOT + path);
            Path realPath;

            try {
                realPath = pathObj.toRealPath();
            } catch (NoSuchFileException e) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
                return;
            }

            if (!realPath.startsWith(DOCUMENT_ROOT)) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
                return;
            } else if (Files.isDirectory(realPath)) {
                String location = "http://" + ((host != null) ? host : SERVER_NAME) + path + "/";
                SendResponse.sendMovePermanentlyResponse(output, location);
                return;
            }

            try (InputStream fis = new BufferedInputStream(Files.newInputStream(realPath))) {
                SendResponse.sendOkResponse(output, fis, ext);
            } catch (FileNotFoundException e) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
