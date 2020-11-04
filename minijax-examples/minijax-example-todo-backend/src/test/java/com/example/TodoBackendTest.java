package com.example;

import static jakarta.ws.rs.HttpMethod.*;
import static jakarta.ws.rs.core.MediaType.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.json.JsonFeature;
import org.minijax.rs.test.MinijaxTest;

import com.example.TodoBackend.Post;

class TodoBackendTest extends MinijaxTest {

    @BeforeEach
    public void setUp() {
        register(JsonFeature.class);
        register(TodoBackend.class);
        getServer().allowCors("/");
    }

    @Test
    void testGetRoot() {
        final Response response = target("/").request().header("Origin", "http://localhost").get();
        assertNotNull(response);
        assertEquals("http://localhost", response.getHeaderString("Access-Control-Allow-Origin"));
    }

    @Test
    void testNewPost() {
        final Response r1 = target("/").request().post(Entity.entity("{\"title\":\"a todo\"}", APPLICATION_JSON));
        assertNotNull(r1);
        assertNotNull(r1.getEntity());
        assertTrue(r1.getEntity() instanceof Post);

        final Post post = (Post) r1.getEntity();
        assertEquals("a todo", post.title);

        final Response r2 = target(post.url).request().get();
        assertNotNull(r2);
        assertEquals(post, r2.getEntity());
    }

    @Test
    void testUpdatePost() {
        final Response r1 = target("/").request().post(Entity.entity("{\"title\":\"initial title\"}", APPLICATION_JSON));
        assertNotNull(r1);
        assertNotNull(r1.getEntity());
        assertTrue(r1.getEntity() instanceof Post);

        final Post post = (Post) r1.getEntity();
        assertEquals("initial title", post.title);

        final Response r2 = target(post.url).request().method(PATCH, Entity.entity("{\"title\":\"bathe the cat\"}", APPLICATION_JSON));
        assertNotNull(r2);

        final Response r3 = target(post.url).request().get();
        assertNotNull(r3);
        assertEquals(post, r3.getEntity());
        assertEquals("bathe the cat", ((Post) r3.getEntity()).title);
    }

    @Test
    void testUpdateOrder() {
        final Response r1 = target("/").request().post(Entity.entity("{\"title\":\"blah\"}", APPLICATION_JSON));
        final Post post = (Post) r1.getEntity();
        target(post.url).request().method(PATCH, Entity.entity("{\"order\":10}", APPLICATION_JSON));
        final Response r3 = target(post.url).request().get();
        assertEquals(10, ((Post) r3.getEntity()).order);
    }

    @Test
    void testSetCompleted() {
        final Response r1 = target("/").request().post(Entity.entity("{\"title\":\"blah\"}", APPLICATION_JSON));
        final Post post = (Post) r1.getEntity();
        target(post.url).request().method(PATCH, Entity.entity("{\"completed\":true}", APPLICATION_JSON));
        final Response r3 = target(post.url).request().get();
        assertTrue(((Post) r3.getEntity()).completed);
    }

    @Test
    void testDeletePost() {
        final Response r1 = target("/").request().post(Entity.entity("{\"title\":\"blah\"}", APPLICATION_JSON));
        final Post post = (Post) r1.getEntity();
        target(post.url).request().delete();
        final Response r3 = target(post.url).request().get();
        assertEquals(404, r3.getStatus());
    }

    @Test
    @SuppressWarnings("rawtypes")
    void testDeleteAll() {
        final Response r1 = target("/").request().delete();
        assertNotNull(r1);

        final Response r2 = target("/").request().get();
        assertNotNull(r2);
        assertTrue(((Collection) r2.getEntity()).isEmpty());
    }
}
