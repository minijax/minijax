package org.minijax.dao.converters;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import jakarta.json.bind.JsonbException;

import org.junit.jupiter.api.Test;

class JsonMapConverterTest {

    @Test
    void testNulls() {
        final JsonMapConverter c = new JsonMapConverter();
        assertNull(c.convertToDatabaseColumn(null));
        assertNull(c.convertToEntityAttribute(null));
    }

    @Test
    void testEmptyString() {
        final JsonMapConverter c = new JsonMapConverter();
        assertNull(c.convertToEntityAttribute(""));
    }

    @Test
    void testBasic() {
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
    void testSpecialChars() {
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

    @Test
    void testInvalidJsonParse() {
        final JsonMapConverter c = new JsonMapConverter();
        assertThrows(JsonbException.class, () -> c.convertToEntityAttribute("{"));
    }

    @Test
    void testInvalidJsonWrite() {
        final JsonMapConverter c = new JsonMapConverter();
        final Map<String, Object> m1 = new HashMap<>();
        m1.put("a", new Object());
        assertThrows(JsonbException.class, () -> c.convertToDatabaseColumn(m1));
    }
}
