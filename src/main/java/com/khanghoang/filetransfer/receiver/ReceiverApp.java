package com.khanghoang.filetransfer.receiver;

import com.khanghoang.filetransfer.receiver.client.SocketClientProvider;
import com.khanghoang.filetransfer.receiver.core.FileReceiver;
import com.khanghoang.filetransfer.receiver.core.FileReceiverImpl;
import com.khanghoang.filetransfer.sender.core.FileSenderImpl;

public class ReceiverApp {
    public static void main(String[] args) {
        String host = "localhost";
        String saveDir =  "C:\\Users\\Ms.Trang\\Downloads\\";
        int port = 9000;
        FileReceiverImpl fileReceiver = new FileReceiverImpl(saveDir);
        new SocketClientProvider(host, port, fileReceiver).connect();
    }
}
