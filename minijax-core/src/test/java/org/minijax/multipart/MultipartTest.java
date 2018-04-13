package org.minijax.multipart;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.ws.rs.WebApplicationException;

import org.junit.Test;
import org.minijax.multipart.Multipart;
import org.minijax.multipart.Part;

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
        try (final Multipart form = new Multipart(Arrays.asList(new Part("a", "b")))) {
            assertEquals("b", form.getString("a"));
        }
    }

    @Test(expected = WebApplicationException.class)
    public void testGetStringException() throws IOException {
        try (final Multipart form = new Multipart(Arrays.asList(new ExplodingPart("a")))) {
            form.getString("a");
        }
    }

    @Test(expected = WebApplicationException.class)
    public void testGetInputStreamException() throws IOException {
        try (final Multipart form = new Multipart(Arrays.asList(new ExplodingPart("a")))) {
            form.getInputStream("a");
        }
    }
}
