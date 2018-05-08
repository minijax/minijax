package org.minijax.cdi;

import static javax.ws.rs.HttpMethod.*;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;

import org.junit.Test;
import org.minijax.Minijax;
import org.minijax.MinijaxApplication;
import org.minijax.MinijaxRequestContext;
import org.minijax.test.MinijaxTestRequestContext;

public class RequestScopedTest {

    @RequestScoped
    private static class A {
    }

    @Test
    public void testRequestScoped() throws IOException {
        final Minijax container = new Minijax();
        final MinijaxApplication application = container.getDefaultApplication();

        A a1;
        A a2;

        try (MinijaxRequestContext context = new MinijaxTestRequestContext(application, GET, "/")) {
            a1 = container.getResource(A.class);
            assertNotNull(a1);
            a2 = container.getResource(A.class);
            assertEquals(a1, a2);
            assertTrue(a1 == a2);
        }

        A a3;
        A a4;

        try (MinijaxRequestContext context = new MinijaxTestRequestContext(application, GET, "/")) {
            a3 = container.getResource(A.class);
            assertNotNull(a3);
            a4 = container.getResource(A.class);
            assertEquals(a3, a4);
            assertTrue(a3 == a4);
        }

        assertNotEquals(a1, a3);
        assertTrue(a1 != a3);

        container.getInjector().close();
    }
}
