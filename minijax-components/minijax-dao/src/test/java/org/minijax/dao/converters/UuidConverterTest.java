package org.minijax.dao.converters;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.minijax.commons.IdUtils;
import org.minijax.dao.converters.UuidConverter;

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
