package com.khanghoang.filetransfer.sender.core;

import com.khanghoang.filetransfer.shared.Protocol;
import com.khanghoang.filetransfer.shared.model.ProtocolChunk;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileSenderImpl implements FileSender {
    private final List<File> files;
    private static final int CHUNK_SIZE = 1024;

    public FileSenderImpl(List<File> files) {
        this.files = files;
    }

    @Override
    public void sendFile(OutputStream out) throws IOException {
        List<Thread> fileThreads = new ArrayList<>();

        for (File file : files) {
            Thread fileThread = new Thread(() -> {
                try {
                    sendSingleFile(file, out);
                } catch (IOException e) {
                    System.err.println("Lỗi khi gửi file " + file.getName() + ": " + e.getMessage());
                }
            });

            fileThread.start();
            fileThreads.add(fileThread);
        }

        for (Thread t : fileThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Thread bị gián đoạn trong quá trình gửi nhiều file.", e);
            }
        }
    }

    private void sendSingleFile(File file, OutputStream out) throws IOException {
        List<ProtocolChunk> chunks = splitFileIntoChunks(file);

        for (ProtocolChunk chunk : chunks) {
            synchronized (out) {
                Protocol.encodeChunk(out, chunk);
                System.out.println("[" + file.getName() + "] Sent chunk " + chunk.getChunkIndex());
            }
        }
    }
    private List<ProtocolChunk> splitFileIntoChunks(File file) throws IOException {
        List<ProtocolChunk> chunks = new ArrayList<>();
        byte[] buffer = new byte[CHUNK_SIZE];

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            int bytesRead;
            int chunkIndex = 0;
            while ((bytesRead = bis.read(buffer)) != -1) {
                byte[] actualData = new byte[bytesRead];
                System.arraycopy(buffer, 0, actualData, 0, bytesRead);

                chunks.add(new ProtocolChunk(
                        file.getName(),
                        chunkIndex,
                        -1, // temporary
                        actualData.length,
                        actualData
                ));

                chunkIndex++;
            }
        }

        int totalChunks = chunks.size();
        for (ProtocolChunk c : chunks) {
            c.setTotalChunks(totalChunks);
        }

        return chunks;
    }
}
