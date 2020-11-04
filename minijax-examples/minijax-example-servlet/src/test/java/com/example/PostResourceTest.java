package com.example;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.client.Entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

class PostResourceTest extends MinijaxTest {

    @BeforeEach
    public void setUp() {
        register(PostResource.class);
    }

    @Test
    void testPost() {
        assertEquals(
                "You posted: xyz",
                target("/post").request().post(Entity.entity("xyz", "text/plain"), String.class));
    }
}
