package org.minijax.rs.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;

import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.UriBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientTest {
    private MinijaxTestClient client;

    @BeforeEach
    void setUp() {
        client = new MinijaxTestClient(null);
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
        assertThrows(UnsupportedOperationException.class, () -> client.register(Object.class, Object.class, Object.class));
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
        assertThrows(UnsupportedOperationException.class, () -> client.register(new Object(), Object.class, Object.class));
    }

    @Test
    void testRegister8() {
        assertThrows(UnsupportedOperationException.class, () -> client.register(new Object(), Collections.emptyMap()));
    }

    @Test
    void testTarget1() {
        assertThrows(UnsupportedOperationException.class, () -> client.target((UriBuilder) null));
    }

    @Test
    void testTarget2() {
        assertThrows(UnsupportedOperationException.class, () -> client.target((Link) null));
    }

    @Test
    void testinvocation() {
        assertThrows(UnsupportedOperationException.class, () -> client.invocation((Link) null));
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
