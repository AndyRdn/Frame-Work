package com.Mapping;

import jakarta.servlet.http.Part;

import java.io.IOException;

public class CustomFile {
    String fileName;
    byte[] bytes;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public CustomFile() {
    }

    public CustomFile(Part part) throws IOException {
        this.fileName = this.getFileName(part);
        this.bytes = part.getInputStream().readAllBytes();
    }


    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] elements = contentDisposition.split(";");
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                return element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
