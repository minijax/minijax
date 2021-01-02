package org.minijax.rs.cdi;

import static jakarta.ws.rs.HttpMethod.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import jakarta.enterprise.context.RequestScoped;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;
import org.minijax.rs.MinijaxApplication;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.test.MinijaxTestRequestContext;

class RequestScopedTest {

    @RequestScoped
    private static class A {
    }

    @Test
    void testRequestScoped() throws IOException {
        final Minijax container = new Minijax();
        final MinijaxApplication application = container.getDefaultApplication();

        A a1;
        A a2;

        try (MinijaxRequestContext context = new MinijaxTestRequestContext(application, GET, "/")) {
            a1 = context.getResource(A.class);
            assertNotNull(a1);
            a2 = context.getResource(A.class);
            assertEquals(a1, a2);
            assertSame(a1, a2);
        }

        A a3;
        A a4;

        try (MinijaxRequestContext context = new MinijaxTestRequestContext(application, GET, "/")) {
            a3 = context.getResource(A.class);
            assertNotNull(a3);
            a4 = context.getResource(A.class);
            assertEquals(a3, a4);
            assertSame(a3, a4);
        }

        assertNotEquals(a1, a3);
        assertNotSame(a1, a3);

        container.getInjector().close();
    }
}
