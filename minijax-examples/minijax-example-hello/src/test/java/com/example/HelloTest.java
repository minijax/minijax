package com.example;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.minijax.rs.test.MinijaxTest;

public class HelloTest extends MinijaxTest {

    @Before
    public void setUp() {
        register(Hello.class);
    }

    @Test
    public void testHello() {
        assertEquals("Hello world!", target("/").request().get(String.class));
    }
}
