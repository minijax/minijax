package org.minijax.db;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class StringListConverterTest {

    @Test
    public void testNull() {
        final StringListConverter c = new StringListConverter();
        assertNotNull(c.convertToDatabaseColumn(null));
        assertNotNull(c.convertToEntityAttribute(null));
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
