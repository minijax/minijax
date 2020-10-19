package org.minijax.rs.delegates;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import jakarta.ws.rs.core.NewCookie;

public class NewCookieDelegateTest {
    private MinijaxNewCookieDelegate d;

    @Before
    public void setUp() {
        d = new MinijaxNewCookieDelegate();
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
        final NewCookie c = d.fromString("a=b");
        assertEquals("a", c.getName());
        assertEquals("b", c.getValue());
    }

    @Test
    public void testSerializeSimple() {
        final NewCookie c = new NewCookie("a", "b");
        assertEquals("a=b", d.toString(c));
    }

    @Test
    public void testSerializeFull() {
        final NewCookie c = new NewCookie("a", "b", "path", "domain", 1, "comment", 101, null, true, true);
        assertEquals("a=b;Path=path;Domain=domain;Max-Age=101;Secure;HttpOnly", d.toString(c));
    }
}
