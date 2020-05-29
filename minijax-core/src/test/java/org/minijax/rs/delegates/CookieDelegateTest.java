package org.minijax.rs.delegates;

import static org.junit.Assert.*;

import jakarta.ws.rs.core.Cookie;

import org.junit.Before;
import org.junit.Test;
import org.minijax.rs.delegates.MinijaxCookieDelegate;

public class CookieDelegateTest {
    private MinijaxCookieDelegate d;

    @Before
    public void setUp() {
        d = new MinijaxCookieDelegate();
    }

    @Test
    public void testDeserializeNull() {
        assertNull(d.fromString(null));
    }

    @Test
    public void testDeserializeEmpty() {
        assertNull(d.fromString(""));
    }

    @Test
    public void testDeserializeSimple() {
        final Cookie c = d.fromString("a=b");
        assertEquals("a", c.getName());
        assertEquals("b", c.getValue());
    }

    @Test
    public void testSerializeSimple() {
        final Cookie c = new Cookie("a", "b");
        assertEquals("a=\"b\"", d.toString(c));
    }

    @Test
    public void testSerializeFull() {
        final Cookie c = new Cookie("a", "b", "path", "domain", 1);
        assertEquals("a=\"b\";$Path=\"path\";$Domain=\"domain\"", d.toString(c));
    }
}
