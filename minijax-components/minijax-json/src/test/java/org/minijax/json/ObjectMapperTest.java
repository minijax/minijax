package org.minijax.json;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.json.bind.Jsonb;

import org.junit.jupiter.api.Test;

class ObjectMapperTest {

    @Test
    void testConstructor() {
        assertThrows(UnsupportedOperationException.class, Json::new);
    }

    @Test
    void testSingleton() {
        final Jsonb m1 = Json.getObjectMapper();
        final Jsonb m2 = Json.getObjectMapper();
        assertSame(m1, m2);
        assertEquals(m1, m2);
    }
}
