package org.minijax.rs.util;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;

import org.junit.jupiter.api.Test;

class CookieUtilsTest {

    @Test
    void testCtor() {
        assertThrows(UnsupportedOperationException.class, CookieUtils::new);
    }

    @Test
    void testDeserializeNullCookie() {
        assertNull(CookieUtils.parseCookie(null));
    }

    @Test
    void testDeserializeEmptyCookie() {
        assertNull(CookieUtils.parseCookie(""));
    }

    @Test
    void testDeserializeSimpleCookie() {
        final Cookie c = CookieUtils.parseCookie("a=b");
        assertEquals("a", c.getName());
        assertEquals("b", c.getValue());
    }

    @Test
    void testSerializeSimpleCookie() {
        final Cookie c = new Cookie("a", "b");
        assertEquals("a=\"b\"", CookieUtils.toString(c));
    }

    @Test
    void testSerializeFullCookie() {
        final Cookie c = new Cookie("a", "b", "path", "domain", 1);
        assertEquals("a=\"b\";$Path=\"path\";$Domain=\"domain\"", CookieUtils.toString(c));
    }

    @Test
    void testDeserializeNullNewCookie() {
        assertNull(CookieUtils.parseNewCookie(null));
    }

    @Test
    void testDeserializeEmptyNewCookie() {
        assertNull(CookieUtils.parseNewCookie(""));
    }

    @Test
    void testDeserializeSimpleNewCookie() {
        final NewCookie c = CookieUtils.parseNewCookie("a=b");
        assertEquals("a", c.getName());
        assertEquals("b", c.getValue());
    }

    @Test
    void testSerializeSimpleNewCookie() {
        final NewCookie c = new NewCookie("a", "b");
        assertEquals("a=b", CookieUtils.toString(c));
    }

    @Test
    void testSerializeFull() {
        final NewCookie c = new NewCookie("a", "b", "path", "domain", 1, "comment", 101, null, true, true);
        assertEquals("a=b;Path=path;Domain=domain;Max-Age=101;Secure;HttpOnly", CookieUtils.toString(c));
    }
}
