package org.minijax.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.Part;
import javax.ws.rs.core.Form;

import org.junit.Test;
import org.minijax.util.IOUtils;

public class MultipartUtilsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new MultipartUtils();
    }

    @Test
    public void testSimple() throws IOException {
        final Form form = new Form();
        form.param("a", "b");

        final InputStream inputStream = MultipartUtils.serializeMultipartForm(form);

        final String str = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        final List<Part> parts = MockPart.parseAll(str);
        assertEquals(1, parts.size());
        assertEquals("a", parts.get(0).getName());
        assertEquals("b", IOUtils.toString(parts.get(0).getInputStream(), StandardCharsets.UTF_8));
    }
}
