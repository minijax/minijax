package org.minijax.mustache;

import static jakarta.ws.rs.core.MediaType.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;
import org.minijax.view.View;

import com.github.mustachejava.MustacheNotFoundException;

class WriterTest extends MinijaxTest {
    private MinijaxMustacheWriter writer;

    @BeforeEach
    public void setUp() {
        register(MustacheFeature.class);
        writer = getServer().getResource(MinijaxMustacheWriter.class);
    }

    @Test
    void testIsWriteable() {
        assertFalse(writer.isWriteable(null, null, null, null));
        assertFalse(writer.isWriteable(Object.class, null, null, null));
        assertTrue(writer.isWriteable(View.class, null, null, null));
    }

    @Test
    void testWriteMap() throws IOException {
        final View view = new View("hello");
        view.getModel().put("key", "value");

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writer.writeTo(view, View.class, null, null, TEXT_HTML_TYPE, null, outputStream);
        assertEquals("key=value", outputStream.toString().trim());
    }

    @Test
    void testWriteWidget() throws IOException {
        final Widget widget = new Widget();
        widget.id = "456";
        widget.value = "foobar";

        final View view = new View("widget");
        view.getModel().put("widget", widget);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writer.writeTo(view, View.class, null, null, TEXT_HTML_TYPE, null, outputStream);

        final String expected =
                "<h1>Widget</h1>\n" +
                "<p>ID: 456</p>\n" +
                "<p>Value: foobar</p>\n";

        assertEquals(expected, outputStream.toString());
    }

    @Test
    void testMissingTemplate() throws IOException {
        final View view = new View("oops");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(MustacheNotFoundException.class, () -> writer.writeTo(view, View.class, null, null, TEXT_HTML_TYPE, null, outputStream));
    }
}
