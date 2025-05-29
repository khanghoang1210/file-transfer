package com.khanghoang.filetransfer.sender.core;

import com.khanghoang.filetransfer.shared.model.ProtocolChunk;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileSenderImpl implements FileSender {
    private final File file;
    private static final int CHUNK_SIZE = 1024;

    public FileSenderImpl(String filePath) {
        this.file = new File(filePath);
    }

    @Override
    public void sendFile(OutputStream out) throws IOException {
        List<ProtocolChunk> chunks = splitFileIntoChunks();
        List<Thread> threads = new ArrayList<>();

        for (ProtocolChunk chunk : chunks) {
            Thread thread = new Thread(new ChunkSenderTask(out, chunk));
            thread.start();
            threads.add(thread);
        }

        // ensure all threads are stopped
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Thread bị gián đoạn trong quá trình gửi file.", e);
            }
        }

        System.out.println("File sent successfully.");
    }
    private List<ProtocolChunk> splitFileIntoChunks() throws IOException {
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
