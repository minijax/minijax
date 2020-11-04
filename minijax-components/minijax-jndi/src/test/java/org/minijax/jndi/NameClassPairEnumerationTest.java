package org.minijax.jndi;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import javax.naming.NameClassPair;

import org.junit.jupiter.api.Test;

class NameClassPairEnumerationTest {

    @Test
    void testEnumeration() {
        final Map<Object, Object> data = new HashMap<>();
        data.put("foo", "bar");

        final MinijaxNameClassPairEnumeration e = new MinijaxNameClassPairEnumeration(data);
        assertTrue(e.hasMore());

        final NameClassPair pair = e.next();
        assertNotNull(pair);
        assertEquals("foo", pair.getName());
        assertEquals("java.lang.String", pair.getClassName());

        assertFalse(e.hasMore());
    }

    @Test
    void testNextElement() {
        assertThrows(UnsupportedOperationException.class, () -> {
        final MinijaxNameClassPairEnumeration e = new MinijaxNameClassPairEnumeration(new HashMap<>());
        e.nextElement();
    });
    }

    @Test
    void testHasMoreElements() {
        assertThrows(UnsupportedOperationException.class, () -> {
        final MinijaxNameClassPairEnumeration e = new MinijaxNameClassPairEnumeration(new HashMap<>());
        e.hasMoreElements();
    });
    }
}
