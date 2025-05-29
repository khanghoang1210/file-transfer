package com.khanghoang.filetransfer.sender.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface MultiFileSender {
    void sendFiles(List<String> filePaths, OutputStream out) throws IOException;
}
