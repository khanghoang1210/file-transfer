package com.khanghoang.filetransfer.sender.server;

import com.khanghoang.filetransfer.sender.core.FileSender;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final FileSender fileSender;

    public ClientHandler(Socket clientSocket, FileSender fileSender) {
        this.clientSocket = clientSocket;
        this.fileSender = fileSender;
    }
    @Override
    public void run() {
        try {
            System.out.println("Connected to client: " + clientSocket.getInetAddress());
            fileSender.sendFile(clientSocket.getOutputStream());
            System.out.println("File sent successfully.");
        } catch (IOException e) {
            System.err.println("Error sending file: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Disconnected from client.");
            } catch (IOException ignored) {
            }
        }
    }

}
