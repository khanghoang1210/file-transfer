package com.khanghoang.filetransfer.sender.core;

import com.khanghoang.filetransfer.shared.Protocol;
import com.khanghoang.filetransfer.shared.model.ProtocolChunk;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ChunkSenderTask implements Runnable {
    private final OutputStream output;
    private final ProtocolChunk chunk;

    public ChunkSenderTask(OutputStream output, ProtocolChunk chunk) {


        this.output = output;
        this.chunk = chunk;
    }

    @Override
    public void run() {
        try {
            synchronized (output) {
                Protocol.encodeChunk(output, chunk);
                System.out.println("Đã gửi chunk " + chunk.getChunkIndex() + "/" + (chunk.getTotalChunks() - 1));
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi gửi chunk: " + e.getMessage());
        }
    }
}
