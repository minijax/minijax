package org.minijax.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.UriBuilder;

import org.minijax.rs.uri.MinijaxUriBuilder;

public class MinijaxClient implements AutoCloseable, Client {
    private final HttpClient httpClient;

    public MinijaxClient() {
        this(HttpClient.newBuilder()
                .version(Version.HTTP_1_1)
                .followRedirects(Redirect.NORMAL)
                .build());
    }

    public MinijaxClient(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpClient getHttpClient() {
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
