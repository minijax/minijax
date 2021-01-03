package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

public class RequestContextTest extends MinijaxTest {

    @Test
    public void testProperties() throws IOException {
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
    public void testSetMethod() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            assertThrows(IllegalStateException.class, () -> ctx.setMethod(null));
        }
    }

    @Test
    public void testSetRequestUri1() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            assertThrows(IllegalStateException.class, () -> ctx.setRequestUri(null));
        }
    }

    @Test
    public void testSetRequestUri2() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            assertThrows(IllegalStateException.class, () -> ctx.setRequestUri(null, null));
        }
    }
}
