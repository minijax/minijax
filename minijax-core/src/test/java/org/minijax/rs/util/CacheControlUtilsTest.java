package org.minijax.rs.util;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.core.CacheControl;
import org.junit.jupiter.api.Test;

class CacheControlUtilsTest {

    @Test
    void testCtor() {
        assertThrows(UnsupportedOperationException.class, CacheControlUtils::new);
    }

    @Test
    void testDeserializeNull() {
        assertNull(CacheControlUtils.fromString(null));
    }

    @Test
    void testDeserializeEmpty() {
        assertNotNull(CacheControlUtils.fromString(""));
    }

    @Test
    void testDeserializePublic() {
        final CacheControl c = CacheControlUtils.fromString("public, max-age=31536000, s-maxage=0");
        assertFalse(c.isPrivate());
        assertEquals(31536000, c.getMaxAge());
        assertEquals(0, c.getSMaxAge());
    }

    @Test
    void testDeserializeNoCache() {
        final CacheControl c = CacheControlUtils.fromString("private, no-cache, no-store, must-revalidate");
        assertTrue(c.isPrivate());
        assertTrue(c.isNoStore());
        assertTrue(c.isMustRevalidate());
    }

    @Test
    void testDeserializeOthers() {
        final CacheControl c = CacheControlUtils.fromString("no-transform, proxy-revalidate");
        assertTrue(c.isNoTransform());
        assertTrue(c.isProxyRevalidate());
    }

    @Test
    void testSerializeNull() {
        assertNull(CacheControlUtils.toString(null));
    }

    @Test
    void testSerializeDefault() {
        assertEquals("public, no-transform", CacheControlUtils.toString(new CacheControl()));
    }

    @Test
    void testSerializeFull() {
        final CacheControl c = new CacheControl();
        c.setMaxAge(100);
        c.setMustRevalidate(true);
        c.setNoCache(true);
        c.setNoStore(true);
        c.setNoTransform(false);
        c.setPrivate(true);
        c.setProxyRevalidate(true);
        c.setSMaxAge(200);
        assertEquals(
                "private, max-age=100, s-maxage=200, must-revalidate, no-cache, no-store, proxy-revalidate",
                CacheControlUtils.toString(c));
    }
}
