package org.minijax.rs.test;

import java.net.URI;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.UriBuilder;

import org.minijax.Minijax;

public class MinijaxTestClient implements jakarta.ws.rs.client.Client {
    private final Minijax server;

    public MinijaxTestClient(final Minijax server) {
        this.server = server;
    }

    @Override
    public MinijaxTestWebTarget target(final String uri) {
        final MinijaxTestWebTarget result = new MinijaxTestWebTarget(server);
        result.getUriBuilder().uri(uri);
        return result;
    }

    @Override
    public MinijaxTestWebTarget target(final URI uri) {
        final MinijaxTestWebTarget result = new MinijaxTestWebTarget(server);
        result.getUriBuilder().uri(uri);
        return result;
    }

    @Override
    public void close() {
        // Nothing to do
    }

    /*
     * Unsupported
     */

    @Override
    public Configuration getConfiguration() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Client property(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Client register(final Class<?> componentClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Client register(final Class<?> componentClass, final int priority) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Client register(final Class<?> componentClass, final Class<?>... contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Client register(final Class<?> componentClass, final Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Client register(final Object component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Client register(final Object component, final int priority) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Client register(final Object component, final Class<?>... contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Client register(final Object component, final Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebTarget target(final UriBuilder uriBuilder) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebTarget target(final Link link) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Builder invocation(final Link link) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SSLContext getSslContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public HostnameVerifier getHostnameVerifier() {
        throw new UnsupportedOperationException();
    }
}
