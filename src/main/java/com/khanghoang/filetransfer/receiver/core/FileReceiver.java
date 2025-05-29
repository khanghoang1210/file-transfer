package com.khanghoang.filetransfer.receiver.core;

import java.io.IOException;
import java.io.InputStream;

public interface FileReceiver {
    void receiveFile(InputStream in) throws IOException;
}
