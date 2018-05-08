package org.minijax.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.minijax.multipart.Multipart;
import org.minijax.multipart.Part;

public class MultipartUtilsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new MultipartUtils();
    }

    @Test
    public void testEmpty() throws IOException {
        final Multipart form = new Multipart();

        final InputStream inputStream = MultipartUtils.serializeMultipartForm(form);

        final Collection<Part> parts = Multipart.read(form.getContentType(), -1, inputStream).getParts();
        assertTrue(parts.isEmpty());
    }

    @Test
    public void testSimple() throws IOException {
        final Multipart form = new Multipart();
        form.param("a", "b");

        final InputStream inputStream = MultipartUtils.serializeMultipartForm(form);

        final List<Part> parts = new ArrayList<>(Multipart.read(form.getContentType(), -1, inputStream).getParts());
        assertEquals(1, parts.size());
        assertEquals("a", parts.get(0).getName());
        assertEquals("b", IOUtils.toString(parts.get(0).getInputStream(), StandardCharsets.UTF_8));
    }

    @Test
    public void testFileUpload() throws IOException {
        final Multipart form = new Multipart();
        form.param("a", "b");
        form.param("myfile", new File("src/test/resources/config.properties"));

        final InputStream inputStream = MultipartUtils.serializeMultipartForm(form);

        final List<Part> parts = new ArrayList<>(Multipart.read(form.getContentType(), -1, inputStream).getParts());
        assertEquals(2, parts.size());
        assertEquals("a", parts.get(0).getName());
        assertEquals("b", IOUtils.toString(parts.get(0).getInputStream(), StandardCharsets.UTF_8));
        assertEquals("myfile", parts.get(1).getName());
        assertEquals("a=b\n", IOUtils.toString(parts.get(1).getInputStream(), StandardCharsets.UTF_8));
    }
}
