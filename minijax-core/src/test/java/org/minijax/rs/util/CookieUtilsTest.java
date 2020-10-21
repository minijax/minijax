package org.minijax.rs.util;

import static org.junit.Assert.*;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;

import org.junit.Test;

public class CookieUtilsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new CookieUtils();
    }

    @Test
    public void testDeserializeNullCookie() {
        assertNull(CookieUtils.parseCookie(null));
    }

    @Test
    public void testDeserializeEmptyCookie() {
        assertNull(CookieUtils.parseCookie(""));
    }

    @Test
    public void testDeserializeSimpleCookie() {
        final Cookie c = CookieUtils.parseCookie("a=b");
        assertEquals("a", c.getName());
        assertEquals("b", c.getValue());
    }

    @Test
    public void testSerializeSimpleCookie() {
        final Cookie c = new Cookie("a", "b");
        assertEquals("a=\"b\"", CookieUtils.toString(c));
    }

    @Test
    public void testSerializeFullCookie() {
        final Cookie c = new Cookie("a", "b", "path", "domain", 1);
        assertEquals("a=\"b\";$Path=\"path\";$Domain=\"domain\"", CookieUtils.toString(c));
    }

    @Test
    public void testDeserializeNullNewCookie() {
        assertNull(CookieUtils.parseNewCookie(null));
    }

    @Test
    public void testDeserializeEmptyNewCookie() {
        assertNull(CookieUtils.parseNewCookie(""));
    }

    @Test
    public void testDeserializeSimpleNewCookie() {
        final NewCookie c = CookieUtils.parseNewCookie("a=b");
        assertEquals("a", c.getName());
        assertEquals("b", c.getValue());
    }

    @Test
    public void testSerializeSimpleNewCookie() {
        final NewCookie c = new NewCookie("a", "b");
        assertEquals("a=b", CookieUtils.toString(c));
    }

    @Test
    public void testSerializeFull() {
        final NewCookie c = new NewCookie("a", "b", "path", "domain", 1, "comment", 101, null, true, true);
        assertEquals("a=b;Path=path;Domain=domain;Max-Age=101;Secure;HttpOnly", CookieUtils.toString(c));
    }
}
