package org.minijax.rs.delegates;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MediaTypeDelegateTest {
    private MinijaxMediaTypeDelegate d;

    @BeforeEach
    public void setUp() {
        d = new MinijaxMediaTypeDelegate();
    }

    @Test
    void testDeserializeNull() {
        assertNull(d.fromString(null));
    }

    @Test
    void testDeserializeEmpty() {
        assertEquals("*", d.fromString("").toString());
    }

    @Test
    void testDeserializeSimple() {
        final MediaType t = d.fromString("a/b");
        assertEquals("a", t.getType());
        assertEquals("b", t.getSubtype());
    }

    @Test
    void testDeserializeParameters() {
        final MediaType t = d.fromString("a/b; c=d");
        assertEquals("a", t.getType());
        assertEquals("b", t.getSubtype());
        assertEquals("d", t.getParameters().get("c"));
    }

    @Test
    void testSerializeSimple() {
        final MediaType t = new MediaType("a", null, (Map<String, String>) null);
        assertEquals("a", d.toString(t));
    }

    @Test
    void testSerializeSubtype() {
        final MediaType t = new MediaType("a", "b", (Map<String, String>) null);
        assertEquals("a/b", d.toString(t));
    }

    @Test
    void testSerializeParameters() {
        final Map<String, String> params = new HashMap<>();
        params.put("c", "d");
        final MediaType t = new MediaType("a", "b", params);
        assertEquals("a/b;c=d", d.toString(t));
    }
}
