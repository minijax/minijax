package org.minijax.cdi;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResourceCacheTest {

    @Test
    public void testSimple() {
        final ResourceCache cm = new ResourceCache();
        final Key<Object> key = Key.of(Object.class);
        final Object obj = new Object();
        cm.put(key, obj);
        assertEquals(obj, cm.get(key));
    }
}
