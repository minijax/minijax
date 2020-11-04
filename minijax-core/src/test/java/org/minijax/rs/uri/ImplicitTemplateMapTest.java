package org.minijax.rs.uri;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ImplicitTemplateMapTest {

    @Test
    void testSimple() {
        final ImplicitTemplateMap map = new ImplicitTemplateMap(new Object[] {"a", "b"});
        assertEquals("a", map.get("foo"));
        assertEquals("a", map.get("foo"));
    }

    @Test
    void testEquals() {
        final ImplicitTemplateMap m1 = new ImplicitTemplateMap(new Object[] {"a", "b"});
        final ImplicitTemplateMap m2 = new ImplicitTemplateMap(new Object[] {"a", "b"});
        final ImplicitTemplateMap m3 = new ImplicitTemplateMap(new Object[] {"c", "d"});
        final ImplicitTemplateMap m4 = new ImplicitTemplateMap(new Object[] {"c", "d"});

        assertEquals(m1, m1);
        assertEquals(m1.hashCode(), m2.hashCode());
        assertEquals(m1, m2);
        assertEquals(m3, m4);
        assertNotEquals(m1, m3);
        assertNotEquals(m1, null);
        assertNotEquals(m1, new Object());

        m4.get("foo");
        assertNotEquals(m3, m4);
    }
}
