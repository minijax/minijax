package org.minijax.rs.delegates;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.core.NewCookie;

class NewCookieDelegateTest {
    private MinijaxNewCookieDelegate d;

    @BeforeEach
    public void setUp() {
        d = new MinijaxNewCookieDelegate();
    }

    @Test
    void testDeserializeNull() {
        assertNull(d.fromString(null));
    }

    @Test
    void testDeserializeEmpty() {
        assertNull(d.fromString(""));
    }

    @Test
    void testDeserializeSimple() {
        final NewCookie c = d.fromString("a=b");
        assertEquals("a", c.getName());
        assertEquals("b", c.getValue());
    }

    @Test
    void testSerializeSimple() {
        final NewCookie c = new NewCookie("a", "b");
        assertEquals("a=b", d.toString(c));
    }

    @Test
    void testSerializeFull() {
        final NewCookie c = new NewCookie("a", "b", "path", "domain", 1, "comment", 101, null, true, true);
        assertEquals("a=b;Path=path;Domain=domain;Max-Age=101;Secure;HttpOnly", d.toString(c));
    }
}
