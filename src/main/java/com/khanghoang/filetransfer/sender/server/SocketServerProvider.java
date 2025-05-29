package com.khanghoang.filetransfer.sender;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerProvider {
    private final int port;
    private final String filePath;

    SocketServerProvider(int port, String filePath) {
        this.port = port;
        this.filePath = filePath;
    }

    void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server đang chạy và lắng nghe tại cổng " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected with client: " + clientSocket.getInetAddress());

                File file = new File(filePath);

                // change to bytestream
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);

                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                dos.writeUTF(file.getName());
                dos.writeLong(file.length());

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }

                dos.flush();
                System.out.println("Đã gửi xong file.");
                bis.close();
                clientSocket.close();
                System.out.println("Đã ngắt kết nối với client.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
