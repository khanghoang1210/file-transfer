package com.khanghoang.filetransfer.sender.server;

import com.khanghoang.filetransfer.sender.core.FileSender;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerProvider {
    private final int port;
    private final FileSender fileSender;

    public SocketServerProvider(int port, FileSender fileSender) {
        this.port = port;
        this.fileSender = fileSender;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server đang chạy và lắng nghe tại cổng " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket,fileSender)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
