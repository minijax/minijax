package com.example;

import static org.junit.Assert.*;

import javax.ws.rs.client.Entity;

import org.junit.Before;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class PostResourceTest extends MinijaxTest {

    @Before
    public void setUp() {
        register(PostResource.class);
    }

    @Test
    public void testPost() {
        assertEquals(
                "You posted: xyz",
                target("/post").request().post(Entity.entity("xyz", "text/plain"), String.class));
    }
}
