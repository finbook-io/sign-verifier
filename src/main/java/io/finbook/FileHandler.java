package io.finbook;

import java.io.*;

public class FileHandler {

    private File file;

    public FileHandler(String path) {
        file = new File(path);
    }

    public byte[] readByteArray() {
        try {
            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            byte[] byteArray = fileInputStream.readAllBytes();
            fileInputStream.close();
            return byteArray;
        } catch (IOException ignored) {
            return null;
        }
    }
}
