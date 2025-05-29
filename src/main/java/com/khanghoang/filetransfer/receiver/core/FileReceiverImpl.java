package com.khanghoang.filetransfer.receiver.core;

import com.khanghoang.filetransfer.shared.Protocol;
import com.khanghoang.filetransfer.shared.model.ProtocolChunk;

import java.io.*;


public class FileReceiverImpl implements FileReceiver {
    private final ChunkReceiverBuffer buffer;

    public FileReceiverImpl(String saveDirectory) {
        this.buffer = new ChunkReceiverBuffer(saveDirectory);
    }
    @Override
    public void receiveFile(InputStream in) throws IOException {
        while (true) {
            try {
                ProtocolChunk chunk = Protocol.decodeChunk(in);
                buffer.receiveChunk(chunk);
            } catch (EOFException eof) {
                System.err.println("⚠ Server đóng kết nối.");
                break;
            } catch (IOException e) {
                System.err.println("Lỗi khi đọc chunk: " + e.getMessage());
                break;
            }
        }
    }
}
