package org.minijax.mustache;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

import com.github.mustachejava.MustacheNotFoundException;

public class WriterTest extends MinijaxTest {
    private MinijaxMustacheWriter writer;

    @Before
    public void setUp() {
        register(MinijaxMustacheFeature.class);
        writer = getServer().get(MinijaxMustacheWriter.class, null, null);
    }

    @Test
    public void testIsWriteable() {
        assertFalse(writer.isWriteable(null, null, null, null));
        assertFalse(writer.isWriteable(Object.class, null, null, null));
        assertTrue(writer.isWriteable(View.class, null, null, null));
    }

    @Test
    public void testWriteMap() throws IOException {
        final View view = new View("hello");
        view.getProps().put("key", "value");

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writer.writeTo(view, View.class, null, null, MediaType.TEXT_HTML_TYPE, null, outputStream);
        assertEquals("key=value", outputStream.toString().trim());
    }

    @Test
    public void testWriteWidget() throws IOException {
        final Widget widget = new Widget();
        widget.id = "456";
        widget.value = "foobar";

        final View view = new View("widget");
        view.getProps().put("widget", widget);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writer.writeTo(view, View.class, null, null, MediaType.TEXT_HTML_TYPE, null, outputStream);

        final String expected =
                "<h1>Widget</h1>\n" +
                "<p>ID: 456</p>\n" +
                "<p>Value: foobar</p>\n";

        assertEquals(expected, outputStream.toString());
    }

    @Test(expected = MustacheNotFoundException.class)
    public void testMissingTemplate() throws IOException {
        final View view = new View("oops");

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writer.writeTo(view, View.class, null, null, MediaType.TEXT_HTML_TYPE, null, outputStream);
    }
}
