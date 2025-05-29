package com.khanghoang.filetransfer.sender;

import com.khanghoang.filetransfer.sender.core.FileSenderImpl;
import com.khanghoang.filetransfer.sender.server.SocketServerProvider;

public class SenderApp {
    public static void main(String[] args) {
        int port = 9000;

        String filePath = "C:\\Users\\Ms.Trang\\Documents\\Progress Test Hoang Khang.docx\\";
        FileSenderImpl fileSender = new FileSenderImpl(filePath);
        SocketServerProvider server = new SocketServerProvider(port, fileSender);
        server.start();
    }
}
