package org.minijax.test;

import java.net.URI;
import java.util.Map;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.minijax.Minijax;

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
    public WebTarget property(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebTarget register(final Class<?> componentClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebTarget register(final Class<?> componentClass, final int priority) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebTarget register(final Class<?> componentClass, final Class<?>... contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebTarget register(final Class<?> componentClass, final Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebTarget register(final Object component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebTarget register(final Object component, final int priority) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebTarget register(final Object component, final Class<?>... contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebTarget register(final Object component, final Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI getUri() {
        return requestUri;
    }

    @Override
    public UriBuilder getUriBuilder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget path(final String path) {
        requestUri = requestUri.resolve(path);
        return this;
    }

    @Override
    public WebTarget resolveTemplate(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebTarget resolveTemplate(final String name, final Object value, final boolean encodeSlashInPath) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebTarget resolveTemplateFromEncoded(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebTarget resolveTemplates(final Map<String, Object> templateValues) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebTarget resolveTemplates(final Map<String, Object> templateValues, final boolean encodeSlashInPath) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebTarget resolveTemplatesFromEncoded(final Map<String, Object> templateValues) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebTarget matrixParam(final String name, final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxWebTarget queryParam(final String name, final Object... values) {
        return this;
    }

    @Override
    public Builder request() {
        return new MinijaxInvocationBuilder(this);
    }

    @Override
    public Builder request(final String... acceptedResponseTypes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Builder request(final MediaType... acceptedResponseTypes) {
        throw new UnsupportedOperationException();
    }
}
