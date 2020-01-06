package org.minijax.rs.cdi;

import static org.junit.Assert.*;

import org.junit.Test;
import org.minijax.cdi.MinijaxInjector;

public class ResourceCacheTest {

    @Test
    public void testSimple() {
        try (MinijaxInjector injector = new MinijaxInjector()) {
            final ResourceCache cm = new ResourceCache();
            final Object key = new Object();
            final Object obj = new Object();
            cm.put(key, obj);
            assertEquals(obj, cm.get(key));
            cm.close();
        }
    }
}
