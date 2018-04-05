package org.minijax.client;

import java.net.URI;
import java.util.Map;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;

import org.minijax.uri.MinijaxUriBuilder;

public class MinijaxClientWebTarget implements javax.ws.rs.client.WebTarget {
    private final MinijaxClient client;
    private final MinijaxUriBuilder uriBuilder;

    public MinijaxClientWebTarget(final MinijaxClient client, final MinijaxUriBuilder uriBuilder) {
        this.client = client;
        this.uriBuilder = uriBuilder;
    }

    public MinijaxClient getClient() {
        return client;
    }

    @Override
    public MinijaxUriBuilder getUriBuilder() {
        return uriBuilder;
    }

    @Override
    public URI getUri() {
        return uriBuilder.build();
    }

    @Override
    public MinijaxClientWebTarget path(final String path) {
        uriBuilder.path(path);
        return this;
    }

    @Override
    public MinijaxClientWebTarget resolveTemplate(final String name, final Object value) {
        uriBuilder.resolveTemplate(name, value);
        return this;
    }

    @Override
    public MinijaxClientWebTarget resolveTemplate(final String name, final Object value, final boolean encodeSlashInPath) {
        uriBuilder.resolveTemplate(name, value, encodeSlashInPath);
        return this;
    }

    @Override
    public MinijaxClientWebTarget resolveTemplateFromEncoded(final String name, final Object value) {
        uriBuilder.resolveTemplateFromEncoded(name, value);
        return this;
    }

    @Override
    public MinijaxClientWebTarget resolveTemplates(final Map<String, Object> templateValues) {
        uriBuilder.resolveTemplates(templateValues);
        return this;
    }

    @Override
    public MinijaxClientWebTarget resolveTemplates(final Map<String, Object> templateValues, final boolean encodeSlashInPath) {
        uriBuilder.resolveTemplates(templateValues, encodeSlashInPath);
        return this;
    }

    @Override
    public MinijaxClientWebTarget resolveTemplatesFromEncoded(final Map<String, Object> templateValues) {
        uriBuilder.resolveTemplatesFromEncoded(templateValues);
        return this;
    }

    @Override
    public MinijaxClientWebTarget matrixParam(final String name, final Object... values) {
        uriBuilder.matrixParam(name, values);
        return this;
    }

    @Override
    public MinijaxClientWebTarget queryParam(final String name, final Object... values) {
        uriBuilder.queryParam(name, values);
        return this;
    }

    @Override
    public MinijaxClientInvocationBuilder request() {
        return new MinijaxClientInvocationBuilder(client, getUri());
    }

    @Override
    public MinijaxClientInvocationBuilder request(final String... acceptedResponseTypes) {
        final MinijaxClientInvocationBuilder builder = request();
        builder.accept(acceptedResponseTypes);
        return builder;
    }

    @Override
    public MinijaxClientInvocationBuilder request(final MediaType... acceptedResponseTypes) {
        final MinijaxClientInvocationBuilder builder = request();
        builder.accept(acceptedResponseTypes);
        return builder;
    }


    /*
     * Unsupported
     */


    @Override
    public Configuration getConfiguration() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientWebTarget property(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientWebTarget register(final Class<?> componentClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientWebTarget register(final Class<?> componentClass, final int priority) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientWebTarget register(final Class<?> componentClass, final Class<?>... contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientWebTarget register(final Class<?> componentClass, final Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientWebTarget register(final Object component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientWebTarget register(final Object component, final int priority) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientWebTarget register(final Object component, final Class<?>... contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxClientWebTarget register(final Object component, final Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException();
    }
}
