package org.minijax.dao.converters;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class StringListConverterTest {

    @Test
    public void testNull() {
        final StringListConverter c = new StringListConverter();
        assertNull(c.convertToDatabaseColumn(null));
        assertTrue(c.convertToEntityAttribute(null).isEmpty());
    }

    @Test
    public void testSimple() {
        final StringListConverter c = new StringListConverter();
        final List<String> list = Arrays.asList("a", "b", "c");
        final String str = "a,b,c";
        assertEquals(str, c.convertToDatabaseColumn(list));
        assertEquals(list, c.convertToEntityAttribute(str));
    }
}
