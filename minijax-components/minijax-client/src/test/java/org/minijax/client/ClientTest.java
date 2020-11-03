package org.minijax.client;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.Collections;

import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.UriBuilder;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ClientTest {

    @Test
    public void testDefaultCtor() {
        try (final MinijaxClient client = new MinijaxClient()) {
            assertNotNull(client.getHttpClient());
        }
    }

    @Test
    public void testOverrideCtor() {
        try (final MinijaxClient client = new MinijaxClient(HttpClient.newHttpClient())) {
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
            assertEquals("http://example.com",
                    client.target(UriBuilder.fromUri("http://example.com")).getUri().toString());
        }
    }

    @Test
    @Disabled("Need Link.Builder")
    public void testTargetLink() {
        try (final MinijaxClient client = new MinijaxClient()) {
            assertEquals("http://example.com",
                    client.target(Link.fromUri("http://example.com").build()).getUri().toString());
        }
    }

    @Test
    public void testGetConfiguration() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.getConfiguration();
            }
        });
    }

    @Test
    public void testProperty() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.property("name", "value");
            }
        });
    }

    @Test
    public void testRegister1() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.register(Object.class);
            }
        });
    }

    @Test
    public void testRegister2() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.register(Object.class, 0);
            }
        });
    }

    @Test
    public void testRegister3() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.register(Object.class, Object.class);
            }
        });
    }

    @Test
    public void testRegister4() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.register(Object.class, Collections.emptyMap());
            }
        });
    }

    @Test
    public void testRegister5() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.register(new Object());
            }
        });
    }

    @Test
    public void testRegister6() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.register(new Object(), 0);
            }
        });
    }

    @Test
    public void testRegister7() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.register(new Object(), Object.class);
            }
        });
    }

    @Test
    public void testRegister8() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.register(new Object(), Collections.emptyMap());
            }
        });
    }

    @Test
    public void testInvocation() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.invocation((Link) null);
            }
        });
    }

    @Test
    public void testGetSslContext() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.getSslContext();
            }
        });
    }

    @Test
    public void testGetHostnameVerifier() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClient client = new MinijaxClient()) {
                client.getHostnameVerifier();
            }
        });
    }
}
