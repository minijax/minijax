package org.minijax.cdi;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;

import javax.enterprise.context.RequestScoped;

import org.junit.Test;
import org.minijax.Minijax;
import org.minijax.MinijaxApplication;
import org.minijax.MinijaxRequestContext;
import org.minijax.test.MockHttpServletRequest;

public class RequestScopedTest {

    @RequestScoped
    public static class A {
    }

    @Test
    public void testRequestScoped() throws IOException {
        final Minijax container = new Minijax();
        final MinijaxApplication application = container.getDefaultApplication();

        final MockHttpServletRequest r1 = new MockHttpServletRequest("GET", URI.create("/"));
        A a1 = null;
        A a2 = null;

        try (MinijaxRequestContext context = new MinijaxRequestContext(application, r1, null)) {
            a1 = container.get(A.class);
            assertNotNull(a1);
            a2 = container.get(A.class);
            assertEquals(a1, a2);
            assertTrue(a1 == a2);
        }

        final MockHttpServletRequest r2 = new MockHttpServletRequest("GET", URI.create("/"));
        A a3 = null;
        A a4 = null;

        try (MinijaxRequestContext context = new MinijaxRequestContext(application, r2, null)) {
            a3 = container.get(A.class);
            assertNotNull(a3);
            a4 = container.get(A.class);
            assertEquals(a3, a4);
            assertTrue(a3 == a4);
        }

        assertNotEquals(a1, a3);
        assertTrue(a1 != a3);
    }
}
