package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.ResourceContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

class ResourceContextTest extends MinijaxTest {

    public static class SubResource {
        @Context
        HttpHeaders headers;
    }

    @Path("/rctest")
    public static class MainResource {
        @Context
        ResourceContext rc;

        @GET
        public String getSubResource() {
            return rc.initResource(new SubResource()).headers.getHeaderString("X-foo");
        }
    }

    @BeforeAll
    public static void setUpResourceContextTest() {
        resetServer();
        register(MainResource.class);
    }

    @Test
    @Disabled("initResource does not currently work with request scoped values")
    void testResourceContextInit() {
        assertEquals("bar", target("/rctest").request().header("X-foo", "bar").get(String.class));
    }
}
