package org.minijax.jndi;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Binding;

import org.junit.jupiter.api.Test;

class BindingsEnumerationTest {

    @Test
    void testEnumeration() {
        final Map<Object, Object> data = new HashMap<>();
        data.put("foo", "bar");

        final MinijaxBindingsEnumeration e = new MinijaxBindingsEnumeration(data);
        assertTrue(e.hasMore());

        final Binding binding = e.next();
        assertNotNull(binding);
        assertEquals("foo", binding.getName());
        assertEquals("bar", binding.getObject());

        assertFalse(e.hasMore());
    }

    @Test
    void testNextElement() {
        assertThrows(UnsupportedOperationException.class, () -> {
        final MinijaxBindingsEnumeration e = new MinijaxBindingsEnumeration(new HashMap<>());
        e.nextElement();
    });
    }

    @Test
    void testHasMoreElements() {
        assertThrows(UnsupportedOperationException.class, () -> {
        final MinijaxBindingsEnumeration e = new MinijaxBindingsEnumeration(new HashMap<>());
        e.hasMoreElements();
    });
    }
}
