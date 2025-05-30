package com.khanghoang.filetransfer.receiver.core;

import com.khanghoang.filetransfer.shared.model.ProtocolChunk;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkReceiverBuffer {
    private final Map<Integer, byte[]> chunkMap = new ConcurrentHashMap<>();
    private final String fileName;
    private final String saveDir;
    private final int totalChunks;
    private int receivedChunks = 0;

    public ChunkReceiverBuffer(String fileName, int totalChunks, String saveDir) {
        this.fileName = fileName;
        this.totalChunks = totalChunks;
        this.saveDir = saveDir;
    }

    public synchronized void receiveChunk(ProtocolChunk chunk) {
        chunkMap.put(chunk.getChunkIndex(), chunk.getData());
        receivedChunks++;

        System.out.println("[" + fileName + "] Nhận chunk " + chunk.getChunkIndex());

        if (receivedChunks == totalChunks) {
            System.out.println("[" + fileName + "] Đủ chunk, bắt đầu ghi file...");
            new Thread(this::assembleFile).start();
        }
    }

    private void assembleFile() {
        try {
            Path savePath = Paths.get(saveDir);
            if (!Files.exists(savePath)) {
                Files.createDirectories(savePath);
            }

            File outputFile = new File(saveDir + File.separator + fileName);
            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                for (int i = 0; i < totalChunks; i++) {
                    bos.write(chunkMap.get(i));
                }
            }

            System.out.println("Đã ghi xong file: " + fileName);
        } catch (IOException e) {
            System.err.println("Lỗi khi ghép file " + fileName + ": " + e.getMessage());
        }
    }
}
