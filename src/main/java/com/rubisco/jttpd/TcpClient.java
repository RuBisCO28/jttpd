package com.rubisco.jttpd;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TcpClient {
    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("localhost", 5173)) {
            FileInputStream fis = new FileInputStream("client_send.txt");
            FileOutputStream fos = new FileOutputStream("client_recv.txt");

            int ch;
            OutputStream output = socket.getOutputStream();
            while ((ch = fis.read()) != -1) {
                output.write(ch);
            }
//            output.write(0);
            InputStream input = socket.getInputStream();
            while ((ch = input.read()) != -1) {
                fos.write(ch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
