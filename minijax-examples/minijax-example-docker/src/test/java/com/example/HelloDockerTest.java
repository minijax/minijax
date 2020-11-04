package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

class HelloDockerTest extends MinijaxTest {

    @Before
    public void setUp() {
        register(HelloDocker.class);
    }

    @Test
    void testHello() {
        assertEquals("Hello world!", target("/").request().get(String.class));
    }
}
