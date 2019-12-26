package org.minijax;

import static org.junit.Assert.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class ResourceContextTest extends MinijaxTest {

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

    @BeforeClass
    public static void setUpResourceContextTest() {
        resetServer();
        register(MainResource.class);
    }

    @Test
    @Ignore("initResource does not currently work with request scoped values")
    public void testResourceContextInit() {
        assertEquals("bar", target("/rctest").request().header("X-foo", "bar").get(String.class));
    }
}
