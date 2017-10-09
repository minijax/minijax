package org.minijax.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClassMapTest {

    @Test
    public void testSimple() {
        final ClassMap cm = new ClassMap();
        final Object obj = new Object();
        cm.put(Object.class, obj);
        assertEquals(obj, cm.get(Object.class));
    }
}
