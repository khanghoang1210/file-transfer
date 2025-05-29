package com.khanghoang.filetransfer.shared;

import com.khanghoang.filetransfer.shared.model.ProtocolChunk;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Protocol {
    public static void encodeChunk(OutputStream out, ProtocolChunk chunk) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        byte[] fileNameBytes = chunk.getFileName().getBytes(StandardCharsets.UTF_8);

        dos.writeInt(fileNameBytes.length);             // Độ dài tên file
        dos.write(fileNameBytes);                       // Tên file
        dos.writeInt(chunk.getChunkIndex());            // Thứ tự chunk
        dos.writeInt(chunk.getTotalChunks());           // Tổng số chunk
        dos.writeInt(chunk.getData().length);           // Kích thước chunk
        dos.write(chunk.getData());                     // Dữ liệu chunk
        dos.flush();
    }

    public static ProtocolChunk decodeChunk(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);

        int fileNameLength = dis.readInt();
        byte[] fileNameBytes = new byte[fileNameLength];
        dis.readFully(fileNameBytes);
        String fileName = new String(fileNameBytes, StandardCharsets.UTF_8);

        int chunkIndex = dis.readInt();
        int totalChunks = dis.readInt();
        int chunkSize = dis.readInt();
        byte[] chunkData = new byte[chunkSize];
        dis.readFully(chunkData);

        return new ProtocolChunk(fileName, chunkIndex, totalChunks, chunkSize, chunkData);
    }
}
