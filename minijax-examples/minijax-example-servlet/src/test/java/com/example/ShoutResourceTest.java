package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

class ShoutResourceTest extends MinijaxTest {

    @BeforeEach
    public void setUp() {
        register(ShoutResource.class);
    }

    @Test
    void testDefault() {
        assertEquals("HELLO FRIEND!", target("/shout").request().get(String.class));
    }

    @Test
    void testQueryString() {
        assertEquals("HELLO CODY!", target("/shout?name=cody").request().get(String.class));
    }
}
