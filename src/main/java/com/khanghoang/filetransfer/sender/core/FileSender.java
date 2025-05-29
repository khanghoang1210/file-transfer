package com.khanghoang.filetransfer.sender.core;

import java.io.IOException;
import java.io.OutputStream;

public interface FileSender {
    void sendFile(OutputStream out) throws IOException;
}
