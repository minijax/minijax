package org.minijax.commons;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

class CopyOnWriteMapTest {

    @Test
    void testBasic() {
        final CopyOnWriteMap<String, String> map = new CopyOnWriteMap<>();
        map.put("a", "b");
        assertEquals("b", map.get("a"));
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());
        assertTrue(map.containsKey("a"));
        assertTrue(map.containsValue("b"));
    }

    @Test
    void testCopyConstructor() {
        final HashMap<String, String> prev = new HashMap<>();
        prev.put("a", "b");

        final CopyOnWriteMap<String, String> map = new CopyOnWriteMap<>(prev);
        assertEquals("b", map.get("a"));
    }

    @Test
    void testPutAll() {
        final HashMap<String, String> prev = new HashMap<>();
        prev.put("a", "b");

        final CopyOnWriteMap<String, String> map = new CopyOnWriteMap<>();
        map.putAll(prev);
        assertEquals("b", map.get("a"));
    }

    @Test
    void testPutIfAbsent() {
        final CopyOnWriteMap<String, String> map = new CopyOnWriteMap<>();
        map.putIfAbsent("a", "b");
        map.putIfAbsent("a", "c");
        assertEquals("b", map.get("a"));
    }

    @Test
    void testRemove() {
        final CopyOnWriteMap<String, String> map = new CopyOnWriteMap<>();
        map.put("a", "b");
        map.remove("a");
        assertNull(map.get("a"));
    }

    @Test
    void testRemoveValue() {
        final CopyOnWriteMap<String, String> map = new CopyOnWriteMap<>();
        map.put("a", "b");
        assertEquals("b", map.get("a"));
        map.remove("a", "c");
        assertEquals("b", map.get("a"));
        map.remove("a", "b");
        assertNull(map.get("a"));
    }

    @Test
    void testReplace() {
        final CopyOnWriteMap<String, String> map = new CopyOnWriteMap<>();
        map.put("a", "b");
        assertEquals("b", map.get("a"));
        map.replace("b", "c");
        assertEquals("b", map.get("a"));
        map.replace("a", "c");
        assertEquals("c", map.get("a"));
    }

    @Test
    void testReplaceValue() {
        final CopyOnWriteMap<String, String> map = new CopyOnWriteMap<>();
        map.put("a", "b");
        assertEquals("b", map.get("a"));
        map.replace("a", "c", "d");
        assertEquals("b", map.get("a"));
        map.replace("a", "b", "d");
        assertEquals("d", map.get("a"));
    }

    @Test
    void testClear() {
        final CopyOnWriteMap<String, String> map = new CopyOnWriteMap<>();
        map.put("a", "b");
        map.clear();
        assertNull(map.get("a"));
        assertTrue(map.isEmpty());
    }

    @Test
    void testContainsKey() {
        final CopyOnWriteMap<String, String> map = new CopyOnWriteMap<>();
        map.put("a", "b");
        assertTrue(map.containsKey("a"));
        assertFalse(map.containsKey("b"));
    }

    @Test
    void testContainsValue() {
        final CopyOnWriteMap<String, String> map = new CopyOnWriteMap<>();
        map.put("a", "b");
        assertFalse(map.containsValue("a"));
        assertTrue(map.containsValue("b"));
    }

    @Test
    void testEntrySet() {
        final HashMap<String, String> prev = new HashMap<>();
        prev.put("a", "b");

        final CopyOnWriteMap<String, String> map = new CopyOnWriteMap<>(prev);
        assertEquals(prev.entrySet(), map.entrySet());
    }
}
