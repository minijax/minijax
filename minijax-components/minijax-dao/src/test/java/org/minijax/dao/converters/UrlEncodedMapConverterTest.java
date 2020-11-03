package org.minijax.dao.converters;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class UrlEncodedMapConverterTest {

    @Test
    public void testNulls() {
        final UrlEncodedMapConverter c = new UrlEncodedMapConverter();
        assertNull(c.convertToDatabaseColumn(null));
        assertNull(c.convertToEntityAttribute(null));
    }

    @Test
    public void testBasic() {
        final UrlEncodedMapConverter c = new UrlEncodedMapConverter();

        final Map<String, String> m1 = new HashMap<>();
        m1.put("a", "b");

        final String s1 = c.convertToDatabaseColumn(m1);
        assertEquals("a=b", s1);

        final Map<String, String> m2 = c.convertToEntityAttribute(s1);
        assertEquals(1, m2.size());
        assertEquals("b", m2.get("a"));
        assertEquals(m1, m2);
    }

    @Test
    public void testSpecialChars() {
        final UrlEncodedMapConverter c = new UrlEncodedMapConverter();

        final Map<String, String> m1 = new HashMap<>();
        m1.put("&", "=");

        final String s1 = c.convertToDatabaseColumn(m1);
        assertEquals("%26=%3d", s1);

        final Map<String, String> m2 = c.convertToEntityAttribute(s1);
        assertEquals(1, m2.size());
        assertEquals("=", m2.get("&"));
        assertEquals(m1, m2);
    }
}
