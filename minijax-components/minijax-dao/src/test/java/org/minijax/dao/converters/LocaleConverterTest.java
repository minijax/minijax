package org.minijax.dao.converters;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;

import org.junit.jupiter.api.Test;

class LocaleConverterTest {

    @Test
    void testNull() {
        final LocaleConverter c = new LocaleConverter();
        assertNull(c.convertToDatabaseColumn(null));
        assertNull(c.convertToEntityAttribute(null));
    }

    @Test
    void testSimple() {
        final LocaleConverter c = new LocaleConverter();
        assertEquals("en-US", c.convertToDatabaseColumn(Locale.US));
        assertEquals(Locale.US, c.convertToEntityAttribute("en-US"));
    }
}
