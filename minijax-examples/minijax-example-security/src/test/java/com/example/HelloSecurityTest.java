package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

public class HelloSecurityTest extends MinijaxTest {

    @BeforeEach
    public void setUp() {
        register(HelloSecurity.class);
        register(HelloSecurity.Security.class);
    }

    @Test
    public void testHello() {
        assertEquals("Hello world!", target("/").request().get(String.class));
    }

    @Test
    public void testSecretUnauthorized() {
        assertEquals(401, target("/secret").request().get().getStatus());
    }

    @Test
    public void testSecretAllowed() {
        assertEquals(
                "Top secret!",
                target("/secret").request().header("Authorization", "bob").get(String.class));
    }

    @Test
    public void testAdminUnauthorized() {
        assertEquals(401, target("/admin").request().get().getStatus());
    }

    @Test
    public void testAdminForbidden() {
        assertEquals(403, target("/admin").request().header("Authorization", "bob").get().getStatus());
    }

    @Test
    public void testAdminAllowed() {
        assertEquals(
                "Admins only!",
                target("/admin").request().header("Authorization", "alice").get(String.class));
    }
}
