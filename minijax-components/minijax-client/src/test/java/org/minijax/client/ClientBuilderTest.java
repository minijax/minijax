package org.minijax.client;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.Test;

class ClientBuilderTest {

    @Test
    void testBuild() {
        assertNotNull(new MinijaxClientBuilder().build());
    }

    @Test
    void testGetConfiguration() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().getConfiguration());
    }

    @Test
    void testProperty() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().property("name", "value"));
    }

    @Test
    void testRegister1() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().register(Object.class));
    }

    @Test
    void testRegister2() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().register(Object.class, 0));
    }

    @Test
    void testRegister3() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().register(Object.class, Object.class));
    }

    @Test
    void testRegister4() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().register(Object.class, Collections.emptyMap()));
    }

    @Test
    void testRegister5() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().register(new Object()));
    }

    @Test
    void testRegister6() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().register(new Object(), 0));
    }

    @Test
    void testRegister7() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().register(new Object(), Object.class));
    }

    @Test
    void testRegister8() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().register(new Object(), Collections.emptyMap()));
    }

    @Test
    void testWithConfig() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().withConfig(null));
    }

    @Test
    void testSslContext() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().sslContext(null));
    }

    @Test
    void testKeyStore() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().keyStore(null, (char[]) null));
    }

    @Test
    void testTrustStore() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().trustStore(null));
    }

    @Test
    void testHostnameVerifier() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().hostnameVerifier(null));
    }

    @Test
    void testExecutorService() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().executorService(null));
    }

    @Test
    void testScheduledExecutorService() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().scheduledExecutorService(null));
    }

    @Test
    void testConnectTimeout() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().connectTimeout(0, null));
    }

    @Test
    void testReadTimeout() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().readTimeout(0, null));
    }
}
