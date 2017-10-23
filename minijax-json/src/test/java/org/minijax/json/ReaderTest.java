package org.minijax.json;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyReader;

import org.junit.Before;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class ReaderTest extends MinijaxTest {
    private MessageBodyReader<?> reader;

    @Before
    public void setUp() {
        register(MinijaxJsonFeature.class);
        reader = getServer().get(MinijaxJsonReader.class);
    }

    @Test
    public void testIsReadable() {
        assertFalse(reader.isReadable(null, null, null, null));
        assertFalse(reader.isReadable(null, null, null, MediaType.TEXT_PLAIN_TYPE));
        assertTrue(reader.isReadable(null, null, null, MediaType.APPLICATION_JSON_TYPE));
    }

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testReadMap() throws IOException {
        final String json = "{\"key\":\"value\"}";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        final MessageBodyReader<Map> mapReader = (MessageBodyReader<Map>) reader;
        final Map<String, String> map = mapReader.readFrom(Map.class, null, null, null, null, inputStream);
        assertEquals("value", map.get("key"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testReadWidget() throws IOException {
        final String json = "{\"id\":\"123\",\"value\":\"hello\"}";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        final MessageBodyReader<Widget> widgetReader = (MessageBodyReader<Widget>) reader;
        final Widget widget = widgetReader.readFrom(Widget.class, null, null, null, null, inputStream);
        assertEquals("123", widget.id);
        assertEquals("hello", widget.value);
    }

    @Test(expected = BadRequestException.class)
    @SuppressWarnings("unchecked")
    public void testReaderException() throws IOException {
        final String json = "";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        final MessageBodyReader<Object> objReader = (MessageBodyReader<Object>) reader;
        objReader.readFrom(Object.class, null, null, null, null, inputStream);
    }
}
