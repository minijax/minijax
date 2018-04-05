package org.minijax.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.Part;

import org.junit.Test;
import org.minijax.MinijaxMultipartForm;
import org.minijax.test.MockPart;
import org.minijax.util.IOUtils;
import org.minijax.util.MultipartUtils;

public class MultipartUtilsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new MultipartUtils();
    }

    @Test
    public void testSimple() throws IOException {
        final MinijaxMultipartForm form = new MinijaxMultipartForm();
        form.param("a", "b");

        final InputStream inputStream = MultipartUtils.serializeMultipartForm(form);

        final String str = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        final List<Part> parts = MockPart.parseAll(str);
        assertEquals(1, parts.size());
        assertEquals("a", parts.get(0).getName());
        assertEquals("b", IOUtils.toString(parts.get(0).getInputStream(), StandardCharsets.UTF_8));
    }

    @Test
    public void testFileUpload() throws IOException {
        final MinijaxMultipartForm form = new MinijaxMultipartForm();
        form.param("a", "b");
        form.param("myfile", new File("src/test/resources/config.properties"));

        final InputStream inputStream = MultipartUtils.serializeMultipartForm(form);

        final String str = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        final List<Part> parts = MockPart.parseAll(str);
        assertEquals(2, parts.size());
        assertEquals("a", parts.get(0).getName());
        assertEquals("b", IOUtils.toString(parts.get(0).getInputStream(), StandardCharsets.UTF_8));
        assertEquals("myfile", parts.get(1).getName());
        assertEquals("a=b\n", IOUtils.toString(parts.get(1).getInputStream(), StandardCharsets.UTF_8));
    }
}
