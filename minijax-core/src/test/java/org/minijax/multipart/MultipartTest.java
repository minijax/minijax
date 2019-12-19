package org.minijax.multipart;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.minijax.MinijaxException;

public class MultipartTest {

    public static class ExplodingInputStream extends InputStream {
        @Override
        public int read() throws IOException {
            throw new IOException("ExplodingInputStream");
        }
    }

    @Test
    public void testSimple() throws IOException {
        try (final Multipart form = new Multipart()) {
            form.param("a", "b");
            assertEquals("b", form.getString("a"));
        }
    }


    @Test(expected = MinijaxException.class)
    public void testGetStringException() throws IOException {
        try (final Multipart form = new Multipart()) {
            form.param("a", "a.txt", new ExplodingInputStream());
            form.getString("a");
        }
    }
}
