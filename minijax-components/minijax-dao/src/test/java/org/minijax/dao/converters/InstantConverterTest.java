package org.minijax.dao.converters;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.time.Instant;

import org.junit.jupiter.api.Test;

class InstantConverterTest {

    @Test
    void testNull() {
        final InstantConverter c = new InstantConverter();
        assertNull(c.convertToDatabaseColumn(null));
        assertNull(c.convertToEntityAttribute(null));
    }

    @Test
    void testSimple() {
        final InstantConverter c = new InstantConverter();
        final Instant instant = Instant.now();
        final Timestamp timestamp = Timestamp.from(instant);
        assertEquals(timestamp, c.convertToDatabaseColumn(instant));
        assertEquals(instant, c.convertToEntityAttribute(timestamp));
    }
}
