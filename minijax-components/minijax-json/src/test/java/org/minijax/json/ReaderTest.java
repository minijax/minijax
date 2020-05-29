package org.minijax.json;

import static jakarta.ws.rs.core.MediaType.*;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.ext.MessageBodyReader;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.test.MinijaxTest;

public class ReaderTest extends MinijaxTest {
    private MinijaxRequestContext context;
    private MessageBodyReader<?> reader;

    @BeforeClass
    public static void setUpReaderTest() {
        register(JsonFeature.class);
    }

    @Before
    public void setUp() {
        context = createRequestContext();
        reader = getServer().getResource(MinijaxJsonReader.class);
    }

    @After
    public void tearDown() throws IOException {
        context.close();
    }

    @Test
    public void testIsReadable() {
        assertFalse(reader.isReadable(null, null, null, null));
        assertFalse(reader.isReadable(null, null, null, TEXT_PLAIN_TYPE));
        assertTrue(reader.isReadable(null, null, null, APPLICATION_JSON_TYPE));
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

    @Test
    @SuppressWarnings("unchecked")
    public void testReadArray() throws IOException {
        final String json = "[{\"id\":\"123\",\"value\":\"hello\"},{\"id\":\"456\",\"value\":\"world\"}]";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        final MessageBodyReader<Widget[]> widgetReader = (MessageBodyReader<Widget[]>) reader;
        final Widget[] widgets = widgetReader.readFrom(Widget[].class, null, null, null, null, inputStream);
        assertEquals(2, widgets.length);
        assertEquals("123", widgets[0].id);
        assertEquals("hello", widgets[0].value);
        assertEquals("456", widgets[1].id);
        assertEquals("world", widgets[1].value);
    }

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testReadGenericList() throws IOException {
        final String json = "[{\"id\":\"123\",\"value\":\"hello\"},{\"id\":\"456\",\"value\":\"world\"}]";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        final MessageBodyReader<List> widgetReader = (MessageBodyReader<List>) reader;
        final List<Widget> widgets = widgetReader.readFrom(List.class, new GenericType<List<Widget>>() {}.getType(), null, null, null, inputStream);
        assertEquals(2, widgets.size());
        assertEquals("123", widgets.get(0).id);
        assertEquals("hello", widgets.get(0).value);
        assertEquals("456", widgets.get(1).id);
        assertEquals("world", widgets.get(1).value);
    }
}
