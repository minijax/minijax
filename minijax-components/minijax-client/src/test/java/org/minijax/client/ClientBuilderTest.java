package org.minijax.client;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.Test;

public class ClientBuilderTest {

    @Test
    public void testBuild() {
        assertNotNull(new MinijaxClientBuilder().build());
    }

    @Test
    public void testGetConfiguration() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().getConfiguration());
    }

    @Test
    public void testProperty() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().property("name", "value"));
    }

    @Test
    public void testRegister1() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().register(Object.class));
    }

    @Test
    public void testRegister2() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().register(Object.class, 0));
    }

    @Test
    public void testRegister3() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().register(Object.class, Object.class));
    }

    @Test
    public void testRegister4() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().register(Object.class, Collections.emptyMap()));
    }

    @Test
    public void testRegister5() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().register(new Object()));
    }

    @Test
    public void testRegister6() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().register(new Object(), 0));
    }

    @Test
    public void testRegister7() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().register(new Object(), Object.class));
    }

    @Test
    public void testRegister8() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().register(new Object(), Collections.emptyMap()));
    }

    @Test
    public void testWithConfig() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().withConfig(null));
    }

    @Test
    public void testSslContext() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().sslContext(null));
    }

    @Test
    public void testKeyStore() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().keyStore(null, (char[]) null));
    }

    @Test
    public void testTrustStore() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().trustStore(null));
    }

    @Test
    public void testHostnameVerifier() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().hostnameVerifier(null));
    }

    @Test
    public void testExecutorService() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().executorService(null));
    }

    @Test
    public void testScheduledExecutorService() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().scheduledExecutorService(null));
    }

    @Test
    public void testConnectTimeout() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().connectTimeout(0, null));
    }

    @Test
    public void testReadTimeout() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxClientBuilder().readTimeout(0, null));
    }
}
