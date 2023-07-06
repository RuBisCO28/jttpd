package com.rubisco.jttpd;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class TcpServer {
    private static final String DOCUMENT_ROOT = ".";
    public static void main(String [] argv) throws Exception {
        try (ServerSocket server = new ServerSocket(8001)) {
            System.out.println("Waiting connection from client");
            Socket socket = server.accept();
            System.out.println("Connected");

            String line = "";
            String path = "";
            InputStream input = socket.getInputStream();
            while ((line = readLine(input)) != null) {
                if (line == "")
                    break;
                if (line.startsWith("GET")) {
                    path = line.split(" ")[1];
                }
            }
            OutputStream output = socket.getOutputStream();
            writeLine(output, "HTTP/1.1 200 OK");
            writeLine(output, "Date: " + getDateSringUtc());
            writeLine(output, "Server: Test/0.1");
            writeLine(output, "Connection: close");
            writeLine(output, "Content-type: text/html");
            writeLine(output, "");

            try (FileInputStream fis = new FileInputStream(DOCUMENT_ROOT + path);) {
                int ch;
                while ((ch = fis.read()) != -1) {
                    output.write(ch);
                }
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readLine(InputStream input) throws Exception {
        int ch;
        String ret = "";
        while ((ch = input.read()) != -1) {
            if (ch == '\r') {

            } else if (ch == '\n') {
                break;
            } else {
                ret += (char)ch;
            }
        }
        if (ch == -1) {
            return null;
        } else {
            return ret;
        }
    }

    private static void writeLine(OutputStream output, String str) throws Exception {
        for (char ch : str.toCharArray()) {
            output.write((int)ch);
        }
        output.write((int)'\r');
        output.write((int)'\n');
    }

    private static String getDateSringUtc() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
        df.setTimeZone(cal.getTimeZone());
        return df.format(cal.getTime()) + " GMT";
    }
}
