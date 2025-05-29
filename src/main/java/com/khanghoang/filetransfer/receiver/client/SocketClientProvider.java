package com.khanghoang.filetransfer.receiver;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClientProvider {
    private final String host;
    private final int port;

    public SocketClientProvider(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        try (Socket socket = new Socket(host, port)) {
            System.out.println("Connected with server at " + host + ":" + port);

            DataInputStream dis = new DataInputStream(socket.getInputStream());

            // Nhận metadata
            String fileName = dis.readUTF();
            long fileSize = dis.readLong();

            FileOutputStream fos = new FileOutputStream("received_" + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            // receive and handle bytestream
            byte[] buffer = new byte[4096];
            int bytesRead;
            long totalRead = 0;
            while (totalRead < fileSize && (bytesRead = dis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
                totalRead += bytesRead;
            }

            bos.flush();
            bos.close();
            System.out.println("Đã nhận file: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
