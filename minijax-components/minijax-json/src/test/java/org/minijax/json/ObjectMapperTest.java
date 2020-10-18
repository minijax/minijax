package org.minijax.json;

import static org.junit.Assert.*;

import javax.json.bind.Jsonb;

import org.junit.Test;

public class ObjectMapperTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new Json();
    }

    @Test
    public void testSingleton() {
        final Jsonb m1 = Json.getObjectMapper();
        final Jsonb m2 = Json.getObjectMapper();
        assertEquals(m1, m2);
        assertTrue(m1 == m2);
    }
}
