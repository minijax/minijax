package org.minijax.dao.converters;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.commons.IdUtils;

class UuidConverterTest {
    private UuidConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new UuidConverter();
    }

    @Test
    void testNull() {
        assertNull(converter.convertToDatabaseColumn(null));
        assertNull(converter.convertToEntityAttribute(null));
    }

    @Test
    void testConvert() {
        final UUID uuid = IdUtils.create();
        final byte[] bytes = IdUtils.toBytes(uuid);

        assertArrayEquals(bytes, converter.convertToDatabaseColumn(uuid));
        assertEquals(uuid, converter.convertToEntityAttribute(bytes));
    }
}
