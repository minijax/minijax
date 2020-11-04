package org.minijax.client;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.Collections;

import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.UriBuilder;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ClientTest {
    private MinijaxClient client;

    @BeforeEach
    void setUp() {
        client = new MinijaxClient();
    }

    @AfterEach
    void tearDown() {
        client.close();
        client = null;
    }

    @Test
    void testDefaultCtor() {
        assertNotNull(client.getHttpClient());
    }

    @Test
    void testOverrideCtor() {
        try (final MinijaxClient client = new MinijaxClient(HttpClient.newHttpClient())) {
            assertNotNull(client.getHttpClient());
        }
    }

    @Test
    void testTargetString() {
        assertEquals("http://example.com", client.target("http://example.com").getUri().toString());
    }

    @Test
    void testTargetUri() {
        assertEquals("http://example.com", client.target(URI.create("http://example.com")).getUri().toString());
    }

    @Test
    void testTargetUriBuilder() {
        assertEquals("http://example.com", client.target(UriBuilder.fromUri("http://example.com")).getUri().toString());
    }

    @Test
    @Disabled("Need Link.Builder")
    void testTargetLink() {
        assertEquals("http://example.com",
                client.target(Link.fromUri("http://example.com").build()).getUri().toString());
    }

    @Test
    void testGetConfiguration() {
        assertThrows(UnsupportedOperationException.class, () -> client.getConfiguration());
    }

    @Test
    void testProperty() {
        assertThrows(UnsupportedOperationException.class, () -> client.property("name", "value"));
    }

    @Test
    void testRegister1() {
        assertThrows(UnsupportedOperationException.class, () -> client.register(Object.class));
    }

    @Test
    void testRegister2() {
        assertThrows(UnsupportedOperationException.class, () -> client.register(Object.class, 0));
    }

    @Test
    void testRegister3() {
        assertThrows(UnsupportedOperationException.class, () -> client.register(Object.class, Object.class));
    }

    @Test
    void testRegister4() {
        assertThrows(UnsupportedOperationException.class, () -> client.register(Object.class, Collections.emptyMap()));
    }

    @Test
    void testRegister5() {
        assertThrows(UnsupportedOperationException.class, () -> client.register(new Object()));
    }

    @Test
    void testRegister6() {
        assertThrows(UnsupportedOperationException.class, () -> client.register(new Object(), 0));
    }

    @Test
    void testRegister7() {
        assertThrows(UnsupportedOperationException.class, () -> client.register(new Object(), Object.class));
    }

    @Test
    void testRegister8() {
        assertThrows(UnsupportedOperationException.class, () -> client.register(new Object(), Collections.emptyMap()));
    }

    @Test
    void testInvocation() {
        assertThrows(UnsupportedOperationException.class, () -> client.invocation(null));
    }

    @Test
    void testGetSslContext() {
        assertThrows(UnsupportedOperationException.class, () -> client.getSslContext());
    }

    @Test
    void testGetHostnameVerifier() {
        assertThrows(UnsupportedOperationException.class, () -> client.getHostnameVerifier());
    }
}
