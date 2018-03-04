package org.minijax.json;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyWriter;

import org.junit.Before;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class WriterTest extends MinijaxTest {
    private MessageBodyWriter<?> writer;

    @Before
    public void setUp() {
        register(JsonFeature.class);
        writer = getServer().getResource(MinijaxJsonWriter.class);
    }

    @Test
    public void testIsWriteable() {
        assertFalse(writer.isWriteable(null, null, null, null));
        assertFalse(writer.isWriteable(null, null, null, MediaType.TEXT_PLAIN_TYPE));
        assertTrue(writer.isWriteable(null, null, null, MediaType.APPLICATION_JSON_TYPE));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testWriteMap() throws IOException {
        final Map<String, String> map = new HashMap<>();
        map.put("key", "value");

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final MessageBodyWriter<Map<String, String>> mapWriter = (MessageBodyWriter<Map<String, String>>) writer;
        mapWriter.writeTo(map, Map.class, null, null, MediaType.APPLICATION_JSON_TYPE, null, outputStream);
        assertEquals("{\"key\":\"value\"}", outputStream.toString());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testWriteWidget() throws IOException {
        final Widget widget = new Widget();
        widget.id = "456";
        widget.value = "foobar";

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final MessageBodyWriter<Widget> widgetWriter = (MessageBodyWriter<Widget>) writer;
        widgetWriter.writeTo(widget, Widget.class, null, null, MediaType.APPLICATION_JSON_TYPE, null, outputStream);
        assertEquals("{\"id\":\"456\",\"value\":\"foobar\"}", outputStream.toString());
    }
}
