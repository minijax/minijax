package org.minijax.dao.converters;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class LocalDateConverterTest {

    @Test
    void testNull() {
        final LocalDateConverter c = new LocalDateConverter();
        assertNull(c.convertToDatabaseColumn(null));
        assertNull(c.convertToEntityAttribute(null));
    }

    @Test
    void testSimple() {
        final LocalDateConverter c = new LocalDateConverter();
        final LocalDate localDate = LocalDate.of(2018, 4, 2);
        final Date sqlDate = Date.valueOf(localDate);
        assertEquals(sqlDate, c.convertToDatabaseColumn(localDate));
        assertEquals(localDate, c.convertToEntityAttribute(sqlDate));
    }
}
