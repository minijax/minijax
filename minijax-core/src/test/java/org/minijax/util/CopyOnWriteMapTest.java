package org.minijax.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class CopyOnWriteMapTest {

    @Test
    public void testBasic() {
        final CopyOnWriteMap<String, String> map = new CopyOnWriteMap<>();
        map.put("a", "b");
        assertEquals("b", map.get("a"));
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());
        assertTrue(map.keySet().contains("a"));
        assertTrue(map.values().contains("b"));
    }

    @Test
    public void testRemove() {
        final CopyOnWriteMap<String, String> map = new CopyOnWriteMap<>();
        map.put("a", "b");
        map.remove("a");
        assertNull(map.get("a"));
    }

    @Test
    public void testClear() {
        final CopyOnWriteMap<String, String> map = new CopyOnWriteMap<>();
        map.put("a", "b");
        map.clear();
        assertNull(map.get("a"));
        assertTrue(map.isEmpty());
    }
}
