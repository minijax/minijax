package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

class HelloResourceTest extends MinijaxTest {

    @BeforeEach
    public void setUp() {
        register(HelloResource.class);
    }

    @Test
    void testHello() {
        assertEquals("Hello world!", target("/").request().get(String.class));
    }
}
