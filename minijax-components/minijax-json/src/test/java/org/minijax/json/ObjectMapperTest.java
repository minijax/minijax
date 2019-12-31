package org.minijax.json;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new Json();
    }

    @Test
    public void testSingleton() {
        final ObjectMapper m1 = Json.getObjectMapper();
        final ObjectMapper m2 = Json.getObjectMapper();
        assertEquals(m1, m2);
        assertTrue(m1 == m2);
    }
}
