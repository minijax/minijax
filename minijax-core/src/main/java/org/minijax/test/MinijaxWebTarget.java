package org.minijax.test;

import java.net.URI;
import java.util.Map;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.minijax.Minijax;
import org.minijax.uri.MinijaxUriBuilder;

public class MinijaxWebTarget implements javax.ws.rs.client.WebTarget {
    private final Minijax server;
    private URI requestUri;

    public MinijaxWebTarget(final Minijax server, final URI requestUri) {
        this.server = server;
        this.requestUri = requestUri;
    }

    public Minijax getServer() {
        return server;
    }

    @Override
    public Configuration getConfiguration() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget property(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget register(final Class<?> componentClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget register(final Class<?> componentClass, final int priority) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget register(final Class<?> componentClass, final Class<?>... contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget register(final Class<?> componentClass, final Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget register(final Object component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget register(final Object component, final int priority) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget register(final Object component, final Class<?>... contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget register(final Object component, final Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI getUri() {
        return requestUri;
    }

    @Override
    public UriBuilder getUriBuilder() {
        return new MinijaxUriBuilder();
    }

    @Override
    public MinijaxWebTarget path(final String path) {
        requestUri = requestUri.resolve(path);
        return this;
    }

    @Override
    public MinijaxWebTarget resolveTemplate(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget resolveTemplate(final String name, final Object value, final boolean encodeSlashInPath) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget resolveTemplateFromEncoded(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget resolveTemplates(final Map<String, Object> templateValues) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget resolveTemplates(final Map<String, Object> templateValues, final boolean encodeSlashInPath) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget resolveTemplatesFromEncoded(final Map<String, Object> templateValues) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget matrixParam(final String name, final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget queryParam(final String name, final Object... values) {
        return this;
    }

    @Override
    public MinijaxInvocationBuilder request() {
        return new MinijaxInvocationBuilder(this);
    }

    @Override
    public MinijaxInvocationBuilder request(final String... acceptedResponseTypes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxInvocationBuilder request(final MediaType... acceptedResponseTypes) {
        throw new UnsupportedOperationException();
    }
}
