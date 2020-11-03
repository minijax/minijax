package org.minijax.rs.multipart;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.minijax.commons.MinijaxException;

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

    @Test
    public void testGetStringException() throws IOException {
        assertThrows(MinijaxException.class, () -> {
            try (final Multipart form = new Multipart()) {
                form.param("a", "a.txt", new ExplodingInputStream());
                form.getString("a");
            }
        });
    }
}
