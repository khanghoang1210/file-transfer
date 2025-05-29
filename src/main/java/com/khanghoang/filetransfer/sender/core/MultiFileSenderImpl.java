package com.khanghoang.filetransfer.sender.core;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MultiFileSenderImpl {
    private FileSenderImpl fileSender;

    public MultiFileSenderImpl(FileSenderImpl fileSender) {
        this.fileSender = fileSender;
    }
    public void sendFiles(List<String> filePaths, OutputStream out) throws IOException {
        for (String path : filePaths) {
            File file = new File(path);
            if (!file.exists() || !file.isFile()) {
                System.err.println("File không hợp lệ: " + path);
                continue;
            }

            fileSender.sendFile(out);
        }

        System.out.println("✅ Gửi tất cả file thành công.");
    }
}





