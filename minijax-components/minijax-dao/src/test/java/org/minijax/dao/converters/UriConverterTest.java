package org.minijax.dao.converters;

import static org.junit.Assert.*;

import java.net.URI;

import org.junit.Test;
import org.minijax.dao.converters.UriConverter;

public class UriConverterTest {

    @Test
    public void testNull() {
        final UriConverter c = new UriConverter();
        assertNull(c.convertToDatabaseColumn(null));
        assertNull(c.convertToEntityAttribute(null));
    }

    @Test
    public void testSimple() {
        final UriConverter c = new UriConverter();
        final URI uri = URI.create("https://www.example.com/path/to/foo?bar=baz");
        final String str = "https://www.example.com/path/to/foo?bar=baz";
        assertEquals(str, c.convertToDatabaseColumn(uri));
        assertEquals(uri, c.convertToEntityAttribute(str));
    }
}
