package org.minijax.client;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

public class ClientBuilderTest {

    @Test
    public void testBuild() {
        assertNotNull(new MinijaxClientBuilder().build());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetConfiguration() {
        new MinijaxClientBuilder().getConfiguration();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testProperty() {
        new MinijaxClientBuilder().property("name", "value");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister1() {
        new MinijaxClientBuilder().register(Object.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister2() {
        new MinijaxClientBuilder().register(Object.class, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister3() {
        new MinijaxClientBuilder().register(Object.class, Object.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister4() {
        new MinijaxClientBuilder().register(Object.class, Collections.emptyMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister5() {
        new MinijaxClientBuilder().register(new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister6() {
        new MinijaxClientBuilder().register(new Object(), 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister7() {
        new MinijaxClientBuilder().register(new Object(), Object.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister8() {
        new MinijaxClientBuilder().register(new Object(), Collections.emptyMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithConfig() {
        new MinijaxClientBuilder().withConfig(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSslContext() {
        new MinijaxClientBuilder().sslContext(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKeyStore() {
        new MinijaxClientBuilder().keyStore(null, (char[]) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTrustStore() {
        new MinijaxClientBuilder().trustStore(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHostnameVerifier() {
        new MinijaxClientBuilder().hostnameVerifier(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExecutorService() {
        new MinijaxClientBuilder().executorService(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testScheduledExecutorService() {
        new MinijaxClientBuilder().scheduledExecutorService(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConnectTimeout() {
        new MinijaxClientBuilder().connectTimeout(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReadTimeout() {
        new MinijaxClientBuilder().readTimeout(0, null);
    }
}
