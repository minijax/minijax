package org.minijax.rs.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.minijax.commons.IOUtils;
import org.minijax.rs.multipart.Multipart;
import org.minijax.rs.multipart.Part;

class MultipartUtilsTest {

    @Test
    void testCtor() {
        assertThrows(UnsupportedOperationException.class, MultipartUtils::new);
    }

    @Test
    void testEmpty() throws IOException {
        final Multipart form = new Multipart();

        final InputStream inputStream = MultipartUtils.serializeMultipartForm(form);

        final Collection<Part> parts = Multipart.read(form.getContentType(), -1, inputStream).getParts();
        assertTrue(parts.isEmpty());
    }

    @Test
    void testSimple() throws IOException {
        final Multipart form = new Multipart();
        form.param("a", "b");

        final InputStream inputStream = MultipartUtils.serializeMultipartForm(form);

        final List<Part> parts = new ArrayList<>(Multipart.read(form.getContentType(), -1, inputStream).getParts());
        assertEquals(1, parts.size());
        assertEquals("a", parts.get(0).getName());
        assertEquals("b", IOUtils.toString(parts.get(0).getInputStream(), StandardCharsets.UTF_8));
    }

    @Test
    void testFileUpload() throws IOException {
        final Multipart form = new Multipart();
        form.param("a", "b");
        form.param("myfile", "config.properties", new FileInputStream("src/test/resources/config.properties"));

        final InputStream inputStream = MultipartUtils.serializeMultipartForm(form);

        final List<Part> parts = new ArrayList<>(Multipart.read(form.getContentType(), -1, inputStream).getParts());
        assertEquals(2, parts.size());
        assertEquals("a", parts.get(0).getName());
        assertEquals("b", IOUtils.toString(parts.get(0).getInputStream(), StandardCharsets.UTF_8));
        assertEquals("myfile", parts.get(1).getName());
        assertEquals("a=b\n", IOUtils.toString(parts.get(1).getInputStream(), StandardCharsets.UTF_8));
    }
}
