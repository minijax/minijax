package org.minijax.cdi;

import static org.junit.Assert.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class ContextProviderTest extends MinijaxTest {

    @GET
    @Path("/context")
    public static String getContext(@Context final ContainerRequestContext context) {
        return context.getClass().getName();
    }

    @BeforeClass
    public static void setUpContextProviderTest() {
        resetServer();
        register(ContextProviderTest.class);
    }

    @Test
    public void testContext() {
        assertEquals("org.minijax.MinijaxRequestContext", target("/context").request().get(String.class));
    }
}
