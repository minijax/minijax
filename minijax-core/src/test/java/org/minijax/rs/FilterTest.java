package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

public class FilterTest extends MinijaxTest {

    public static class MyFilter implements ContainerRequestFilter {
        static MyFilter lastInstance;

        @Override
        public void filter(final ContainerRequestContext requestContext) throws IOException {
            lastInstance = this;
        }
    }

    @GET
    @Path("/")
    public static String get() {
        return "Hello";
    }

    @BeforeAll
    public static void setUpFilterTest() {
        resetServer();
        register(FilterTest.class);
        register(MyFilter.class);
    }

    @Test
    public void testFeature() {
        target("/").request().get();
        assertNotNull(MyFilter.lastInstance);
    }
}
