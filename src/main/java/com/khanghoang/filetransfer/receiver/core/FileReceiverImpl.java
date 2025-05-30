package com.khanghoang.filetransfer.receiver.core;

import com.khanghoang.filetransfer.shared.Protocol;
import com.khanghoang.filetransfer.shared.model.ProtocolChunk;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FileReceiverImpl implements FileReceiver {
    private final String saveDirectory;
    private final Map<String, ChunkReceiverBuffer> bufferMap = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(8);

    public FileReceiverImpl(String saveDirectory) {
        this.saveDirectory = saveDirectory;
    }
    @Override
    public void receiveFile(InputStream in) throws IOException {
        while (true) {
            try {
                ProtocolChunk chunk = Protocol.decodeChunk(in);

                // to handle chunk in parallel
                executor.submit(() -> {
                    String fileName = chunk.getFileName();

                    bufferMap.computeIfAbsent(
                            fileName,
                            f -> new ChunkReceiverBuffer(fileName, chunk.getTotalChunks(), saveDirectory)
                    ).receiveChunk(chunk);
                });
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
