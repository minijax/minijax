package org.minijax.client;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.Collections;

import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.UriBuilder;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void testDefaultCtor() {
        try (final MinijaxClient client = new MinijaxClient()) {
            assertNotNull(client.getHttpClient());
        }
    }

    @Test
    void testOverrideCtor() {
        try (final MinijaxClient client = new MinijaxClient(HttpClient.newHttpClient())) {
            assertNotNull(client.getHttpClient());
        }
    }

    @Test
    void testTargetString() {
        try (final MinijaxClient client = new MinijaxClient()) {
            assertEquals("http://example.com", client.target("http://example.com").getUri().toString());
        }
    }

    @Test
    void testTargetUri() {
        try (final MinijaxClient client = new MinijaxClient()) {
            assertEquals("http://example.com", client.target(URI.create("http://example.com")).getUri().toString());
        }
    }

    @Test
    void testTargetUriBuilder() {
        try (final MinijaxClient client = new MinijaxClient()) {
            assertEquals("http://example.com",
                    client.target(UriBuilder.fromUri("http://example.com")).getUri().toString());
        }
    }

    @Test
    @Disabled("Need Link.Builder")
    void testTargetLink() {
        try (final MinijaxClient client = new MinijaxClient()) {
            assertEquals("http://example.com",
                    client.target(Link.fromUri("http://example.com").build()).getUri().toString());
        }
    }

    @Test
    void testGetConfiguration() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.getConfiguration();
            }
        });
    }

    @Test
    void testProperty() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.property("name", "value");
            }
        });
    }

    @Test
    void testRegister1() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.register(Object.class);
            }
        });
    }

    @Test
    void testRegister2() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.register(Object.class, 0);
            }
        });
    }

    @Test
    void testRegister3() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.register(Object.class, Object.class);
            }
        });
    }

    @Test
    void testRegister4() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.register(Object.class, Collections.emptyMap());
            }
        });
    }

    @Test
    void testRegister5() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.register(new Object());
            }
        });
    }

    @Test
    void testRegister6() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.register(new Object(), 0);
            }
        });
    }

    @Test
    void testRegister7() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.register(new Object(), Object.class);
            }
        });
    }

    @Test
    void testRegister8() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.register(new Object(), Collections.emptyMap());
            }
        });
    }

    @Test
    void testInvocation() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.invocation(null);
            }
        });
    }

    @Test
    void testGetSslContext() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.getSslContext();
            }
        });
    }

    @Test
    void testGetHostnameVerifier() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.getHostnameVerifier();
            }
        });
    }
}
