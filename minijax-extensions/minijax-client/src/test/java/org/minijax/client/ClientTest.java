package org.minijax.client;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.Collections;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;

import org.apache.http.impl.client.HttpClients;
import org.junit.Ignore;
import org.junit.Test;

public class ClientTest {

    @Test
    public void testDefaultCtor() {
        try (final MinijaxClient client = new MinijaxClient()) {
            assertNotNull(client.getHttpClient());
        }
    }

    @Test
    public void testOverrideCtor() {
        try (final MinijaxClient client = new MinijaxClient(HttpClients.createMinimal())) {
            assertNotNull(client.getHttpClient());
        }
    }

    @Test
    public void testTargetString() {
        try (final MinijaxClient client = new MinijaxClient()) {
            assertEquals("http://example.com", client.target("http://example.com").getUri().toString());
        }
    }

    @Test
    public void testTargetUri() {
        try (final MinijaxClient client = new MinijaxClient()) {
            assertEquals("http://example.com", client.target(URI.create("http://example.com")).getUri().toString());
        }
    }

    @Test
    public void testTargetUriBuilder() {
        try (final MinijaxClient client = new MinijaxClient()) {
            assertEquals("http://example.com", client.target(UriBuilder.fromUri("http://example.com")).getUri().toString());
        }
    }

    @Test
    @Ignore("Need Link.Builder")
    public void testTargetLink() {
        try (final MinijaxClient client = new MinijaxClient()) {
            assertEquals("http://example.com", client.target(Link.fromUri("http://example.com").build()).getUri().toString());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetConfiguration() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.getConfiguration();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testProperty() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.property("name", "value");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister1() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.register(Object.class);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister2() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.register(Object.class, 0);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister3() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.register(Object.class, Object.class);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister4() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.register(Object.class, Collections.emptyMap());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister5() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.register(new Object());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister6() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.register(new Object(), 0);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister7() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.register(new Object(), Object.class);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister8() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.register(new Object(), Collections.emptyMap());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvocation() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.invocation((Link) null);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSslContext() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.getSslContext();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetHostnameVerifier() {
        try (final MinijaxClient client = new MinijaxClient()) {
            client.getHostnameVerifier();
        }
    }
}
