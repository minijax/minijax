package org.minijax.rs.cdi;

import static jakarta.ws.rs.HttpMethod.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.minijax.Minijax;
import org.minijax.rs.MinijaxApplicationContext;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.test.MinijaxTestRequestContext;

import jakarta.enterprise.context.RequestScoped;

public class RequestScopedTest {

    @RequestScoped
    private static class A {
    }

    @Test
    public void testRequestScoped() throws IOException {
        final Minijax container = new Minijax();
        final MinijaxApplicationContext application = container.getDefaultApplication();

        A a1;
        A a2;

        try (MinijaxRequestContext context = new MinijaxTestRequestContext(application, GET, "/")) {
            a1 = context.getResource(A.class);
            assertNotNull(a1);
            a2 = context.getResource(A.class);
            assertEquals(a1, a2);
            assertTrue(a1 == a2);
        }

        A a3;
        A a4;

        try (MinijaxRequestContext context = new MinijaxTestRequestContext(application, GET, "/")) {
            a3 = context.getResource(A.class);
            assertNotNull(a3);
            a4 = context.getResource(A.class);
            assertEquals(a3, a4);
            assertTrue(a3 == a4);
        }

        assertNotEquals(a1, a3);
        assertTrue(a1 != a3);

        container.getInjector().close();
    }
}
