package org.minijax.multipart;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Part {
    private final String name;
    private String value;
    private String submittedFileName;
    private InputStream inputStream;
    private File file;

    private Part(final String name) {
        this.name = name;
    }

    public Part(final String name, final String value) {
        this(name);
        this.value = value;
    }

    public Part(final String name, final String submittedFileName, final InputStream inputStream) {
        this(name);
        this.submittedFileName = submittedFileName;
        this.inputStream = inputStream;
    }

    public Part(final String name, final File file) {
        this(name);
        this.file = file;
        submittedFileName = file.getName();
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getSubmittedFileName() {
        return submittedFileName;
    }

    public InputStream getInputStream() throws IOException {
        if (inputStream == null) {
            if (file != null) {
                inputStream = new FileInputStream(file);
            } else if (value != null) {
                inputStream = new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8));
            } else {
                inputStream = new ByteArrayInputStream(new byte[0]);
            }
        }
        return inputStream;
    }
}
