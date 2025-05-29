package com.khanghoang.filetransfer.receiver.client;

import com.khanghoang.filetransfer.receiver.core.FileReceiver;

import java.io.*;
import java.net.Socket;

public class SocketClientProvider {
    private final String host;
    private final int port;
    private FileReceiver fileReceiver;

    public SocketClientProvider(String host, int port, FileReceiver fileReceiver) {
        this.host = host;
        this.port = port;
        this.fileReceiver = fileReceiver;
    }

    public void connect() {
        try (Socket socket = new Socket(host, port)) {
            System.out.println("Connected with server at " + host + ":" + port);

            DataInputStream dis = new DataInputStream(socket.getInputStream());

            fileReceiver.receiveFile(dis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
