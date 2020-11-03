package org.minijax.rs.delegates;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.core.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CookieDelegateTest {
    private MinijaxCookieDelegate d;

    @BeforeEach
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
