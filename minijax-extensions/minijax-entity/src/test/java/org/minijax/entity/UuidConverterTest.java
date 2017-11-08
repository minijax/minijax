package org.minijax.entity;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.minijax.util.IdUtils;

public class UuidConverterTest {
    private UuidConverter converter;


    @Before
    public void setUp() {
        converter = new UuidConverter();
    }


    @Test
    public void testNull() {
        assertNull(converter.convertToDatabaseColumn(null));
        assertNull(converter.convertToEntityAttribute(null));
    }


    @Test
    public void testConvert() {
        final UUID uuid = IdUtils.create();
        final byte[] bytes = IdUtils.toBytes(uuid);

        assertArrayEquals(bytes, converter.convertToDatabaseColumn(uuid));
        assertEquals(uuid, converter.convertToEntityAttribute(bytes));
    }
}
