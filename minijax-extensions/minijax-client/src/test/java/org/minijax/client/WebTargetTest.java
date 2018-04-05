package org.minijax.client;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;
import org.minijax.uri.MinijaxUriBuilder;

public class WebTargetTest {

    @Test
    public void testCtor() {
        try (final MinijaxClient client = new MinijaxClient()) {
            final MinijaxClientWebTarget target = client.target("http://foo.com");
            assertEquals(client, target.getClient());
            assertNotNull(target.getUriBuilder());
            assertEquals("http://foo.com", target.getUri().toString());
        }
    }

    @Test
    public void testPath() {
        final MinijaxClientWebTarget target = new MinijaxClientWebTarget(null, new MinijaxUriBuilder().uri("http://foo.com"));
        target.path("/bar");
        assertEquals("http://foo.com/bar", target.getUri().toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResolveTemplate() {
        final MinijaxClientWebTarget target = new MinijaxClientWebTarget(null, new MinijaxUriBuilder().uri("http://foo.com/{x}"));
        target.resolveTemplate("x", "bar");
        assertEquals("http://foo.com/bar", target.getUri().toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetConfiguration() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.target("/").getConfiguration();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testProperty() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.target("/").property("name", "value");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister1() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.target("/").register(Object.class);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister2() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.target("/").register(Object.class, 0);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister3() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.target("/").register(Object.class, Object.class);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister4() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.target("/").register(Object.class, Collections.emptyMap());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister5() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.target("/").register(new Object());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister6() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.target("/").register(new Object(), 0);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister7() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.target("/").register(new Object(), Object.class);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister8() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.target("/").register(new Object(), Collections.emptyMap());
        }
    }
}
