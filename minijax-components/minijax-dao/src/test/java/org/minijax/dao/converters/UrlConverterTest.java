package org.minijax.dao.converters;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.minijax.MinijaxException;
import org.minijax.dao.converters.UrlConverter;

public class UrlConverterTest {

    @Test
    public void testNull() {
        final UrlConverter c = new UrlConverter();
        assertNull(c.convertToDatabaseColumn(null));
        assertNull(c.convertToEntityAttribute(null));
    }

    @Test
    public void testSimple() throws MalformedURLException {
        final UrlConverter c = new UrlConverter();
        final URL url = new URL("https://www.example.com/path/to/foo?bar=baz");
        final String str = "https://www.example.com/path/to/foo?bar=baz";
        assertEquals(str, c.convertToDatabaseColumn(url));
        assertEquals(url, c.convertToEntityAttribute(str));
    }

    @Test(expected = MinijaxException.class)
    public void testMalformedUrl() throws MalformedURLException {
        final UrlConverter c = new UrlConverter();
        final String str = "! bad url !";
        c.convertToEntityAttribute(str);
    }
}
