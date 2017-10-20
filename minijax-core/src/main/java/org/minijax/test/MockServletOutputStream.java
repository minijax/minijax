package org.minijax.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class MockServletOutputStream extends ServletOutputStream {
    private final ByteArrayOutputStream outputStream;

    public MockServletOutputStream() {
        outputStream = new ByteArrayOutputStream();
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(final WriteListener writeListener) {
    }

    @Override
    public void write(final int b) throws IOException {
        outputStream.write(b);
    }


    public String getValue() throws IOException {
        outputStream.flush();
        return outputStream.toString();
    }
}
