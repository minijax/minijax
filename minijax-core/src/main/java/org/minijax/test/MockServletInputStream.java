package org.minijax.test;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class MockServletInputStream extends ServletInputStream {
    private final InputStream inner;
    private int next;

    public MockServletInputStream(final InputStream inner) {
        this.inner = inner;
        try {
            read();
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean isFinished() {
        return next != -1;
    }

    @Override
    public boolean isReady() {
        return !isFinished();
    }

    @Override
    public void setReadListener(final ReadListener readListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read() throws IOException {
        final int result = next;
        next = inner.read();
        return result;
    }
}
