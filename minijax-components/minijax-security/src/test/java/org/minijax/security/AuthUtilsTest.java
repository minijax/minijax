package org.minijax.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AuthUtilsTest {

    @Test
    void testHappyPath() {
        final String auth = "Basic Y29keTpzZWNyZXQ=";
        assertEquals("cody", AuthUtils.getUsername(auth));
        assertEquals("secret", AuthUtils.getPassword(auth));
    }

    @Test
    void testNull() {
        final String auth = null;
        assertNull(AuthUtils.getUsername(auth));
        assertNull(AuthUtils.getPassword(auth));
    }

    @Test
    void testNonBasic() {
        final String auth = "Bearer Y29keTpzZWNyZXQ=";
        assertNull(AuthUtils.getUsername(auth));
        assertNull(AuthUtils.getPassword(auth));
    }

    @Test
    void testEmptyBase64() {
        final String auth = "Basic ";
        assertNull(AuthUtils.getUsername(auth));
        assertNull(AuthUtils.getPassword(auth));
    }

    @Test
    void testInvalidBase64() {
        final String auth = "Basic xxxx";
        assertNull(AuthUtils.getUsername(auth));
        assertNull(AuthUtils.getPassword(auth));
    }

    @Test
    void testWhitespace() {
        final String auth = "Basic    ";
        assertNull(AuthUtils.getUsername(auth));
        assertNull(AuthUtils.getPassword(auth));
    }

    @Test
    void testConstructor() {
        try {
            new AuthUtils();
            fail("Expected UnsupportedOperationException");
        } catch (final UnsupportedOperationException ex) {
            assertNotNull(ex);
        }
    }
}
