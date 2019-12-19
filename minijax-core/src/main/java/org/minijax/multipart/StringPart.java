package org.minijax.multipart;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StringPart implements Part{
    private final String name;
    private final String value;
    private InputStream inputStream;

    StringPart(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getSubmittedFileName() {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (inputStream == null) {
            inputStream = new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8));
        }
        return inputStream;
    }
}
