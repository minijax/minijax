package org.minijax.rs.cdi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.minijax.cdi.MinijaxInjector;

class ResourceCacheTest {

    @Test
    void testSimple() {
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
