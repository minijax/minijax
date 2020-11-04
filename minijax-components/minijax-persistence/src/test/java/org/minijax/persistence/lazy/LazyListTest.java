package org.minijax.persistence.lazy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

class LazyListTest {

    @Test
    void testIsEmpty() {
        final LazyList<String> list1 = new LazyList<>(new MockQuery<>());
        assertTrue(list1.isEmpty());

        final LazyList<String> list2 = new LazyList<>(new MockQuery<>("foo"));
        assertFalse(list2.isEmpty());
    }

    @Test
    void testContains() {
        final LazyList<String> list = new LazyList<>(new MockQuery<>("foo"));
        assertTrue(list.contains("foo"));
        assertFalse(list.contains("bar"));
    }

    @Test
    void testContainsAll() {
        final LazyList<String> list = new LazyList<>(new MockQuery<>("foo", "bar"));
        assertTrue(list.containsAll(Arrays.asList("foo", "bar")));
        assertFalse(list.containsAll(Arrays.asList("foo", "bar", "baz")));
    }

    @Test
    void testIterator() {
        final LazyList<String> list = new LazyList<>(new MockQuery<>("foo"));
        final Iterator<String> iter = list.iterator();
        assertTrue(iter.hasNext());
        assertEquals("foo", iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    void testAdd() {
        final LazyList<String> list = new LazyList<>(new MockQuery<>("foo"));
        assertFalse(list.contains("bar"));
        list.add("bar");
        assertTrue(list.contains("bar"));
    }

    @Test
    void testAddAll() {
        final LazyList<String> list = new LazyList<>(new MockQuery<>("foo"));
        assertFalse(list.contains("bar"));
        list.addAll(Arrays.asList("bar", "baz"));
        assertEquals(Arrays.asList("foo", "bar", "baz"), list);
    }

    @Test
    void testAddAllAtIndex() {
        final LazyList<String> list = new LazyList<>(new MockQuery<>("foo"));
        assertFalse(list.contains("bar"));
        list.addAll(0, Arrays.asList("bar", "baz"));
        assertEquals(Arrays.asList("bar", "baz", "foo"), list);
    }

    @Test
    void testClear() {
        final LazyList<String> list = new LazyList<>(new MockQuery<>("foo"));
        assertTrue(list.contains("foo"));
        list.clear();
        assertFalse(list.contains("foo"));
    }
}
