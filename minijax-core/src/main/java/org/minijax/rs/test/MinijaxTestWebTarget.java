package org.minijax.rs.test;

import java.net.URI;
import java.util.Map;

import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;

import org.minijax.Minijax;
import org.minijax.rs.uri.MinijaxUriBuilder;

public class MinijaxTestWebTarget implements jakarta.ws.rs.client.WebTarget {
    private final Minijax server;
    private final MinijaxUriBuilder uriBuilder;

    public MinijaxTestWebTarget(final Minijax server) {
        this.server = server;
        this.uriBuilder = new MinijaxUriBuilder();
    }

    public Minijax getServer() {
        return server;
    }

    @Override
    public Configuration getConfiguration() {
        return server.getDefaultApplication().getConfiguration();
    }

    @Override
    public UriBuilder getUriBuilder() {
        return uriBuilder;
    }

    @Override
    public MinijaxTestWebTarget path(final String path) {
        uriBuilder.path(path);
        return this;
    }

    @Override
    public MinijaxTestWebTarget queryParam(final String name, final Object... values) {
        uriBuilder.queryParam(name, values);
        return this;
    }

    @Override
    public URI getUri() {
        URI result = uriBuilder.build();

        // Remove trailing slashes
        final String str = result.toString();
        if (str.length() > 1 && str.endsWith("/")) {
            result = URI.create(str.substring(0, str.length() - 1));
        }

        return result;
    }

    @Override
    public MinijaxTestInvocationBuilder request() {
        return new MinijaxTestInvocationBuilder(this);
    }

    /*
     * Unsupported
     */

    @Override
    public MinijaxTestWebTarget property(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestWebTarget register(final Class<?> componentClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestWebTarget register(final Class<?> componentClass, final int priority) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestWebTarget register(final Class<?> componentClass, final Class<?>... contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestWebTarget register(final Class<?> componentClass, final Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestWebTarget register(final Object component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestWebTarget register(final Object component, final int priority) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestWebTarget register(final Object component, final Class<?>... contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestWebTarget register(final Object component, final Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestWebTarget resolveTemplate(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestWebTarget resolveTemplate(final String name, final Object value, final boolean encodeSlashInPath) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestWebTarget resolveTemplateFromEncoded(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestWebTarget resolveTemplates(final Map<String, Object> templateValues) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestWebTarget resolveTemplates(final Map<String, Object> templateValues, final boolean encodeSlashInPath) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestWebTarget resolveTemplatesFromEncoded(final Map<String, Object> templateValues) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestWebTarget matrixParam(final String name, final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestInvocationBuilder request(final String... acceptedResponseTypes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestInvocationBuilder request(final MediaType... acceptedResponseTypes) {
        throw new UnsupportedOperationException();
    }
}
