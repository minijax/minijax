package org.minijax.json;

import static jakarta.ws.rs.core.MediaType.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.MessageBodyWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

class WriterTest extends MinijaxTest {
    private MessageBodyWriter<?> writer;

    @BeforeEach
    public void setUp() {
        register(JsonFeature.class);
        writer = getServer().getResource(MinijaxJsonWriter.class);
    }

    @Test
    void testIsWriteable() {
        assertFalse(writer.isWriteable(null, null, null, null));
        assertFalse(writer.isWriteable(null, null, null, TEXT_PLAIN_TYPE));
        assertTrue(writer.isWriteable(null, null, null, APPLICATION_JSON_TYPE));
        assertTrue(writer.isWriteable(null, null, null, MediaType.valueOf("application/foo+json")));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testWriteMap() throws IOException {
        final Map<String, String> map = new HashMap<>();
        map.put("key", "value");

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final MessageBodyWriter<Map<String, String>> mapWriter = (MessageBodyWriter<Map<String, String>>) writer;
        mapWriter.writeTo(map, Map.class, null, null, APPLICATION_JSON_TYPE, null, outputStream);
        assertEquals("{\"key\":\"value\"}", outputStream.toString());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testWriteWidget() throws IOException {
        final Widget widget = new Widget();
        widget.id = "456";
        widget.value = "foobar";

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final MessageBodyWriter<Widget> widgetWriter = (MessageBodyWriter<Widget>) writer;
        widgetWriter.writeTo(widget, Widget.class, null, null, APPLICATION_JSON_TYPE, null, outputStream);
        assertEquals("{\"id\":\"456\",\"value\":\"foobar\"}", outputStream.toString());
    }
}
