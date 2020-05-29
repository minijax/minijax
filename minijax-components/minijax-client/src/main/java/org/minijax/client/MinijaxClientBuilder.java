package org.minijax.client;

import java.security.KeyStore;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Configuration;

public class MinijaxClientBuilder extends ClientBuilder {

    @Override
    public Client build() {
        return new MinijaxClient();
    }

    /*
     * Unsupported
     */

    @Override
    public Configuration getConfiguration() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder property(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder register(final Class<?> componentClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder register(final Class<?> componentClass, final int priority) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder register(final Class<?> componentClass, final Class<?>... contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder register(final Class<?> componentClass, final Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder register(final Object component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder register(final Object component, final int priority) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder register(final Object component, final Class<?>... contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder register(final Object component, final Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder withConfig(final Configuration config) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder sslContext(final SSLContext sslContext) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder keyStore(final KeyStore keyStore, final char[] password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder trustStore(final KeyStore trustStore) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder hostnameVerifier(final HostnameVerifier verifier) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder executorService(final ExecutorService executorService) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder scheduledExecutorService(final ScheduledExecutorService scheduledExecutorService) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder connectTimeout(final long timeout, final TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientBuilder readTimeout(final long timeout, final TimeUnit unit) {
        throw new UnsupportedOperationException();
    }
}
