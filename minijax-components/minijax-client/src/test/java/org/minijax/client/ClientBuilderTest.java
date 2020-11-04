package org.minijax.client;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientBuilderTest {
    private MinijaxClientBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new MinijaxClientBuilder();
    }

    @Test
    void testBuild() {
        assertNotNull(builder.build());
    }

    @Test
    void testGetConfiguration() {
        assertThrows(UnsupportedOperationException.class, () -> builder.getConfiguration());
    }

    @Test
    void testProperty() {
        assertThrows(UnsupportedOperationException.class, () -> builder.property("name", "value"));
    }

    @Test
    void testRegister1() {
        assertThrows(UnsupportedOperationException.class, () -> builder.register(Object.class));
    }

    @Test
    void testRegister2() {
        assertThrows(UnsupportedOperationException.class, () -> builder.register(Object.class, 0));
    }

    @Test
    void testRegister3() {
        assertThrows(UnsupportedOperationException.class, () -> builder.register(Object.class, Object.class));
    }

    @Test
    void testRegister4() {
        assertThrows(UnsupportedOperationException.class, () -> builder.register(Object.class, Collections.emptyMap()));
    }

    @Test
    void testRegister5() {
        assertThrows(UnsupportedOperationException.class, () -> builder.register(new Object()));
    }

    @Test
    void testRegister6() {
        assertThrows(UnsupportedOperationException.class, () -> builder.register(new Object(), 0));
    }

    @Test
    void testRegister7() {
        assertThrows(UnsupportedOperationException.class, () -> builder.register(new Object(), Object.class));
    }

    @Test
    void testRegister8() {
        assertThrows(UnsupportedOperationException.class, () -> builder.register(new Object(), Collections.emptyMap()));
    }

    @Test
    void testWithConfig() {
        assertThrows(UnsupportedOperationException.class, () -> builder.withConfig(null));
    }

    @Test
    void testSslContext() {
        assertThrows(UnsupportedOperationException.class, () -> builder.sslContext(null));
    }

    @Test
    void testKeyStore() {
        assertThrows(UnsupportedOperationException.class, () -> builder.keyStore(null, (char[]) null));
    }

    @Test
    void testTrustStore() {
        assertThrows(UnsupportedOperationException.class, () -> builder.trustStore(null));
    }

    @Test
    void testHostnameVerifier() {
        assertThrows(UnsupportedOperationException.class, () -> builder.hostnameVerifier(null));
    }

    @Test
    void testExecutorService() {
        assertThrows(UnsupportedOperationException.class, () -> builder.executorService(null));
    }

    @Test
    void testScheduledExecutorService() {
        assertThrows(UnsupportedOperationException.class, () -> builder.scheduledExecutorService(null));
    }

    @Test
    void testConnectTimeout() {
        assertThrows(UnsupportedOperationException.class, () -> builder.connectTimeout(0, null));
    }

    @Test
    void testReadTimeout() {
        assertThrows(UnsupportedOperationException.class, () -> builder.readTimeout(0, null));
    }
}
