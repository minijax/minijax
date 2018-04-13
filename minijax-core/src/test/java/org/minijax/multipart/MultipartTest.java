package org.minijax.multipart;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.WebApplicationException;

import org.junit.Test;

public class MultipartTest {

    public static class ExplodingPart extends Part {

        public ExplodingPart(final String name) {
            super(name, "");
        }

        @Override
        public InputStream getInputStream() throws IOException {
            throw new IOException("ExplodingPart");
        }
    }

    @Test
    public void testSimple() throws IOException {
        try (final Multipart form = new Multipart()) {
            form.param("a", "b");
            assertEquals("b", form.getString("a"));
        }
    }

    @Test(expected = WebApplicationException.class)
    public void testGetStringException() throws IOException {
        try (final Multipart form = new Multipart()) {
            form.param(new ExplodingPart("a"));
            form.getString("a");
        }
    }
}
