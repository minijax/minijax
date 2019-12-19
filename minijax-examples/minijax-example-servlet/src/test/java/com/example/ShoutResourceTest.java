package com.example;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class ShoutResourceTest extends MinijaxTest {

    @Before
    public void setUp() {
        register(ShoutResource.class);
    }

    @Test
    public void testDefault() {
        assertEquals("HELLO FRIEND!", target("/shout").request().get(String.class));
    }

    @Test
    public void testQueryString() {
        assertEquals("HELLO CODY!", target("/shout?name=cody").request().get(String.class));
    }
}
