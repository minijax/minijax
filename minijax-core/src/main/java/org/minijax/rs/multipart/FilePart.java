package org.minijax.rs.multipart;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.minijax.commons.IOUtils;
import org.minijax.rs.MinijaxException;

public class FilePart implements Part {
    private final String name;
    private final String fileName;
    private final InputStream inputStream;
    private String value;

    FilePart(final String name, final String fileName, final InputStream inputStream) {
        this.name = name;
        this.fileName = fileName;
        this.inputStream = inputStream;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSubmittedFileName() {
        return fileName;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    @Override
    public String getValue() {
        if (value == null) {
            try {
                value = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            } catch (final IOException ex) {
                throw new MinijaxException(ex);
            }
        }
        return value;
    }
}
