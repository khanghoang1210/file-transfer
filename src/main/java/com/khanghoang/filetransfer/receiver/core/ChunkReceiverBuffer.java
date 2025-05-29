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
    private final Map<Integer, byte[]> chunkMap  = new ConcurrentHashMap<>();
    private int totalChunks = -1;
    private String fileName = "";
    private final String saveDir;

    public ChunkReceiverBuffer(String saveDir) {
        this.saveDir = saveDir;
    }

    public void receiveChunk(ProtocolChunk chunk) {
        if (fileName.isEmpty()) {
            fileName = chunk.getFileName();
            totalChunks = chunk.getTotalChunks();
        }

        chunkMap.put(chunk.getChunkIndex(), chunk.getData());
        System.out.println("Nhận chunk " + chunk.getChunkIndex());

        if (chunkMap.size() == totalChunks) {
            try {
                assembleFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void assembleFile() throws IOException {
        Path savePath = Paths.get(saveDir);
        if (!Files.exists(savePath)) {
            Files.createDirectories(savePath);
        }

        File outputFile = new File(saveDir + File.separator + fileName);
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            for (int i = 0; i < totalChunks; i++) {
                bos.write(chunkMap.get(i));
            }
            bos.flush();
        }

        System.out.println("Đã ghép file thành công: " + fileName);
    }
}
