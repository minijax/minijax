package org.minijax.dao.converters;

import static org.junit.jupiter.api.Assertions.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.minijax.commons.MinijaxException;

class UrlConverterTest {

    @Test
    void testNull() {
        final UrlConverter c = new UrlConverter();
        assertNull(c.convertToDatabaseColumn(null));
        assertNull(c.convertToEntityAttribute(null));
    }

    @Test
    void testSimple() throws MalformedURLException {
        final UrlConverter c = new UrlConverter();
        final URL url = new URL("https://www.example.com/path/to/foo?bar=baz");
        final String str = "https://www.example.com/path/to/foo?bar=baz";
        assertEquals(str, c.convertToDatabaseColumn(url));
        assertEquals(url, c.convertToEntityAttribute(str));
    }

    @Test
    void testMalformedUrl() throws MalformedURLException {
        assertThrows(MinijaxException.class, () -> {
        final UrlConverter c = new UrlConverter();
        final String str = "! bad url !";
        c.convertToEntityAttribute(str);
    });
    }
}
