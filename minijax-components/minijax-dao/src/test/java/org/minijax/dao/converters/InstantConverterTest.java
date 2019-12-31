package org.minijax.dao.converters;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.time.Instant;

import org.junit.Test;
import org.minijax.dao.converters.InstantConverter;

public class InstantConverterTest {

    @Test
    public void testNull() {
        final InstantConverter c = new InstantConverter();
        assertNull(c.convertToDatabaseColumn(null));
        assertNull(c.convertToEntityAttribute(null));
    }

    @Test
    public void testSimple() {
        final InstantConverter c = new InstantConverter();
        final Instant instant = Instant.now();
        final Timestamp timestamp = Timestamp.from(instant);
        assertEquals(timestamp, c.convertToDatabaseColumn(instant));
        assertEquals(instant, c.convertToEntityAttribute(timestamp));
    }
}
