package com.khanghoang.filetransfer.sender;

import com.khanghoang.filetransfer.sender.core.FileSenderImpl;
import com.khanghoang.filetransfer.sender.server.SocketServerProvider;

import java.io.File;
import java.util.List;

public class SenderApp {
    public static void main(String[] args) {
        int port = 9000;

        List<File> filesToSend = List.of(
                new File("C:\\Users\\Ms.Trang\\Documents\\Progress Test Hoang Khang.docx\\"),
                new File("C:\\Users\\Ms.Trang\\Documents\\hosts.txt\\")
        );

        FileSenderImpl fileSender = new FileSenderImpl(filesToSend);
        SocketServerProvider server = new SocketServerProvider(port, fileSender);
        server.start();
    }
}
