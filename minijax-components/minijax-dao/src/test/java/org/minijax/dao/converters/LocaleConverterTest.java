package org.minijax.dao.converters;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;
import org.minijax.dao.converters.LocaleConverter;

public class LocaleConverterTest {

    @Test
    public void testNull() {
        final LocaleConverter c = new LocaleConverter();
        assertNull(c.convertToDatabaseColumn(null));
        assertNull(c.convertToEntityAttribute(null));
    }

    @Test
    public void testSimple() {
        final LocaleConverter c = new LocaleConverter();
        assertEquals("en-US", c.convertToDatabaseColumn(Locale.US));
        assertEquals(Locale.US, c.convertToEntityAttribute("en-US"));
    }
}
