package org.minijax.jndi;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Binding;

import org.junit.Test;

public class BindingsEnumerationTest {

    @Test
    public void testEnumeration() {
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

    @Test(expected = UnsupportedOperationException.class)
    public void testNextElement() {
        final MinijaxBindingsEnumeration e = new MinijaxBindingsEnumeration(new HashMap<>());
        e.nextElement();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHasMoreElements() {
        final MinijaxBindingsEnumeration e = new MinijaxBindingsEnumeration(new HashMap<>());
        e.hasMoreElements();
    }
}
