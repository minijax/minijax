package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

class RequestContextTest extends MinijaxTest {

    @Test
    void testProperties() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            ctx.setProperty("a", "b");
            assertEquals("b", ctx.getProperty("a"));
            assertEquals(1, ctx.getPropertyNames().size());
            assertTrue(ctx.getPropertyNames().contains("a"));

            ctx.setProperty("a", "c");
            assertEquals("c", ctx.getProperty("a"));

            ctx.removeProperty("a");
            assertNull(ctx.getProperty("a"));
            assertTrue(ctx.getPropertyNames().isEmpty());
        }
    }

    @Test
    void testSetMethod() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            assertThrows(IllegalStateException.class, () -> ctx.setMethod(null));
        }
    }

    @Test
    void testSetRequestUri1() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            assertThrows(IllegalStateException.class, () -> ctx.setRequestUri(null));
        }
    }

    @Test
    void testSetRequestUri2() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            assertThrows(IllegalStateException.class, () -> ctx.setRequestUri(null, null));
        }
    }
}
