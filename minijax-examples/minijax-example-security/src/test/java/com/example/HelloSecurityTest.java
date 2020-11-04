package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

class HelloSecurityTest extends MinijaxTest {

    @BeforeEach
    public void setUp() {
        register(HelloSecurity.class);
        register(HelloSecurity.Security.class);
    }

    @Test
    void testHello() {
        assertEquals("Hello world!", target("/").request().get(String.class));
    }

    @Test
    void testSecretUnauthorized() {
        assertEquals(401, target("/secret").request().get().getStatus());
    }

    @Test
    void testSecretAllowed() {
        assertEquals(
                "Top secret!",
                target("/secret").request().header("Authorization", "bob").get(String.class));
    }

    @Test
    void testAdminUnauthorized() {
        assertEquals(401, target("/admin").request().get().getStatus());
    }

    @Test
    void testAdminForbidden() {
        assertEquals(403, target("/admin").request().header("Authorization", "bob").get().getStatus());
    }

    @Test
    void testAdminAllowed() {
        assertEquals(
                "Admins only!",
                target("/admin").request().header("Authorization", "alice").get(String.class));
    }
}
