package org.minijax.rs.delegates;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;
import org.minijax.rs.delegates.MinijaxMediaTypeDelegate;

public class MediaTypeDelegateTest {
    private MinijaxMediaTypeDelegate d;

    @Before
    public void setUp() {
        d = new MinijaxMediaTypeDelegate();
    }

    @Test
    public void testDeserializeNull() {
        assertNull(d.fromString(null));
    }

    @Test
    public void testDeserializeEmpty() {
        assertEquals("*", d.fromString("").toString());
    }

    @Test
    public void testDeserializeSimple() {
        final MediaType t = d.fromString("a/b");
        assertEquals("a", t.getType());
        assertEquals("b", t.getSubtype());
    }

    @Test
    public void testDeserializeParameters() {
        final MediaType t = d.fromString("a/b; c=d");
        assertEquals("a", t.getType());
        assertEquals("b", t.getSubtype());
        assertEquals("d", t.getParameters().get("c"));
    }

    @Test
    public void testSerializeSimple() {
        final MediaType t = new MediaType("a", null, (Map<String, String>) null);
        assertEquals("a", d.toString(t));
    }

    @Test
    public void testSerializeSubtype() {
        final MediaType t = new MediaType("a", "b", (Map<String, String>) null);
        assertEquals("a/b", d.toString(t));
    }

    @Test
    public void testSerializeParameters() {
        final Map<String, String> params = new HashMap<>();
        params.put("c", "d");
        final MediaType t = new MediaType("a", "b", params);
        assertEquals("a/b;c=d", d.toString(t));
    }
}
