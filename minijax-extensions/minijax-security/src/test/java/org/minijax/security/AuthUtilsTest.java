package org.minijax.security;

import static org.junit.Assert.*;

import org.junit.Test;

public class AuthUtilsTest {

    @Test
    public void testHappyPath() {
        final String auth = "Basic Y29keTpzZWNyZXQ=";
        assertEquals("cody", AuthUtils.getUsername(auth));
        assertEquals("secret", AuthUtils.getPassword(auth));
    }

    @Test
    public void testNull() {
        final String auth = null;
        assertNull(AuthUtils.getUsername(auth));
        assertNull(AuthUtils.getPassword(auth));
    }

    @Test
    public void testNonBasic() {
        final String auth = "Bearer Y29keTpzZWNyZXQ=";
        assertNull(AuthUtils.getUsername(auth));
        assertNull(AuthUtils.getPassword(auth));
    }

    @Test
    public void testEmptyBase64() {
        final String auth = "Basic ";
        assertNull(AuthUtils.getUsername(auth));
        assertNull(AuthUtils.getPassword(auth));
    }

    @Test
    public void testInvalidBase64() {
        final String auth = "Basic xxxx";
        assertNull(AuthUtils.getUsername(auth));
        assertNull(AuthUtils.getPassword(auth));
    }

    @Test
    public void testWhitespace() {
        final String auth = "Basic    ";
        assertNull(AuthUtils.getUsername(auth));
        assertNull(AuthUtils.getPassword(auth));
    }

    @Test
    public void testConstructor() {
        try {
            new AuthUtils();
            fail("Expected UnsupportedOperationException");
        } catch (final UnsupportedOperationException ex) {
            assertNotNull(ex);
        }
    }
}
