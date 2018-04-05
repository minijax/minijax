package org.minijax.client;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.minijax.MinijaxException;
import org.minijax.uri.MinijaxUriBuilder;

public class MinijaxClient implements AutoCloseable, Client {
    private final CloseableHttpClient httpClient;

    public MinijaxClient() {
        this(HttpClients.createDefault());
    }

    public MinijaxClient(final CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public MinijaxClientWebTarget target(final String uri) {
        return new MinijaxClientWebTarget(this, new MinijaxUriBuilder().uri(uri));
    }

    @Override
    public MinijaxClientWebTarget target(final URI uri) {
        return new MinijaxClientWebTarget(this, new MinijaxUriBuilder().uri(uri));
    }

    @Override
    public MinijaxClientWebTarget target(final UriBuilder uriBuilder) {
        return new MinijaxClientWebTarget(this, (MinijaxUriBuilder) uriBuilder);
    }

    @Override
    public MinijaxClientWebTarget target(final Link link) {
        return new MinijaxClientWebTarget(this, new MinijaxUriBuilder().uri(link.getUri()));
    }

    @Override
    public void close() {
        try {
            httpClient.close();
        } catch (final IOException ex) {
            throw new MinijaxException(ex);
        }
    }


    /*
     * Unsupported
     */


    @Override
    public Configuration getConfiguration() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClient property(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClient register(final Class<?> componentClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClient register(final Class<?> componentClass, final int priority) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClient register(final Class<?> componentClass, final Class<?>... contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClient register(final Class<?> componentClass, final Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClient register(final Object component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClient register(final Object component, final int priority) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClient register(final Object component, final Class<?>... contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClient register(final Object component, final Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientInvocationBuilder invocation(final Link link) {
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
