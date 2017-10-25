package org.minijax.entity;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class JsonMapConverterTest {

    @Test
    public void testNulls() {
        final JsonMapConverter c = new JsonMapConverter();
        assertNull(c.convertToDatabaseColumn(null));
        assertNull(c.convertToEntityAttribute(null));
    }

    @Test
    public void testBasic() {
        final JsonMapConverter c = new JsonMapConverter();

        final Map<String, Object> m1 = new HashMap<>();
        m1.put("a", "b");

        final String s1 = c.convertToDatabaseColumn(m1);
        assertEquals("{\"a\":\"b\"}", s1);

        final Map<String, Object> m2 = c.convertToEntityAttribute(s1);
        assertEquals(1, m2.size());
        assertEquals("b", m2.get("a"));
        assertEquals(m1, m2);
    }

    @Test
    public void testSpecialChars() {
        final JsonMapConverter c = new JsonMapConverter();

        final Map<String, Object> m1 = new HashMap<>();
        m1.put("&", "=");

        final String s1 = c.convertToDatabaseColumn(m1);
        assertEquals("{\"&\":\"=\"}", s1);

        final Map<String, Object> m2 = c.convertToEntityAttribute(s1);
        assertEquals(1, m2.size());
        assertEquals("=", m2.get("&"));
        assertEquals(m1, m2);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidJsonParse() {
        final JsonMapConverter c = new JsonMapConverter();
        c.convertToEntityAttribute("{");
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidJsonWrite() {
        final JsonMapConverter c = new JsonMapConverter();

        final Map<String, Object> m1 = new HashMap<>();
        m1.put("a", new Object());

        c.convertToDatabaseColumn(m1);
    }
}
