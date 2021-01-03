package org.minijax.rs.test;

import static jakarta.ws.rs.HttpMethod.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.ws.rs.client.AsyncInvoker;
import jakarta.ws.rs.client.CompletionStageRxInvoker;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.client.RxInvoker;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

import org.minijax.Minijax;
import org.minijax.rs.MinijaxApplication;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.MinijaxUriInfo;
import org.minijax.rs.util.EntityUtils;
import org.minijax.rs.util.ExceptionUtils;

public class MinijaxTestInvocationBuilder implements jakarta.ws.rs.client.Invocation.Builder {
    private final MinijaxTestWebTarget target;
    private final MultivaluedMap<String, String> headers;
    private final Map<String, Cookie> cookies;
    private Entity<?> entity;

    public MinijaxTestInvocationBuilder(final MinijaxTestWebTarget target) {
        this.target = target;
        headers = new MultivaluedHashMap<>();
        cookies = new HashMap<>();
    }

    @Override
    public Response get() {
        return method(GET);
    }

    @Override
    public <T> T get(final Class<T> responseType) {
        return method(GET, responseType);
    }

    @Override
    public <T> T get(final GenericType<T> responseType) {
        return method(GET, responseType);
    }

    @Override
    public Response put(final Entity<?> entity) {
        return method(PUT, entity);
    }

    @Override
    public <T> T put(final Entity<?> entity, final Class<T> responseType) {
        return method(PUT, entity, responseType);
    }

    @Override
    public <T> T put(final Entity<?> entity, final GenericType<T> responseType) {
        return method(PUT, entity, responseType);
    }

    @Override
    public Response post(final Entity<?> entity) {
        return method(POST, entity);
    }

    @Override
    public <T> T post(final Entity<?> entity, final Class<T> responseType) {
        return method(POST, entity, responseType);
    }

    @Override
    public <T> T post(final Entity<?> entity, final GenericType<T> responseType) {
        return method(POST, entity, responseType);
    }

    @Override
    public Response delete() {
        return method(DELETE);
    }

    @Override
    public <T> T delete(final Class<T> responseType) {
        return method(DELETE, responseType);
    }

    @Override
    public <T> T delete(final GenericType<T> responseType) {
        return method(DELETE, responseType);
    }

    @Override
    public Response head() {
        return method(HEAD);
    }

    @Override
    public Response options() {
        return method(OPTIONS);
    }

    @Override
    public <T> T options(final Class<T> responseType) {
        return method(OPTIONS, responseType);
    }

    @Override
    public <T> T options(final GenericType<T> responseType) {
        return method(OPTIONS, responseType);
    }

    @Override
    public Response trace() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T trace(final Class<T> responseType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T trace(final GenericType<T> responseType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Response method(final String name) {
        final Minijax container = target.getServer();
        final MinijaxApplication application = container.getApplication(target.getUri());
        final MinijaxUriInfo uriInfo = new MinijaxUriInfo(target.getUri());

        if (!cookies.isEmpty()) {
            headers.add("Cookie", cookies.values().stream()
                    .map(c -> c.getName() + "=" + c.getValue())
                    .collect(Collectors.joining("; ")));
        }

        try (final MinijaxRequestContext clientContext = new MinijaxTestRequestContext(application, name, uriInfo);
                final InputStream entityStream = EntityUtils.writeEntity(entity, clientContext.getProviders());
                final MinijaxRequestContext serverContext = new MinijaxTestRequestContext(
                        application,
                        name,
                        new MinijaxUriInfo(target.getUri()),
                        new MinijaxTestHttpHeaders(headers),
                        entityStream)) {
            return application.handle(serverContext);

        } catch (final IOException ex) {
            throw ExceptionUtils.toWebAppException(ex);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T method(final String name, final Class<T> responseType) {
        return (T) method(name).getEntity();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T method(final String name, final GenericType<T> responseType) {
        return (T) method(name).getEntity();
    }

    @Override
    public Response method(final String name, final Entity<?> entity) {
        setEntity(entity);
        return method(name);
    }

    @Override
    public <T> T method(final String name, final Entity<?> entity, final Class<T> responseType) {
        setEntity(entity);
        return method(name, responseType);
    }

    @Override
    public <T> T method(final String name, final Entity<?> entity, final GenericType<T> responseType) {
        setEntity(entity);
        return method(name, responseType);
    }

    @Override
    public Invocation build(final String method) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Invocation build(final String method, final Entity<?> entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Invocation buildGet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Invocation buildDelete() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Invocation buildPost(final Entity<?> entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Invocation buildPut(final Entity<?> entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AsyncInvoker async() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Builder accept(final String... mediaTypes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Builder accept(final MediaType... mediaTypes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Builder acceptLanguage(final Locale... locales) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Builder acceptLanguage(final String... locales) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Builder acceptEncoding(final String... encodings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestInvocationBuilder cookie(final Cookie cookie) {
        cookies.put(cookie.getName(), cookie);
        return this;
    }

    @Override
    public MinijaxTestInvocationBuilder cookie(final String name, final String value) {
        return cookie(new Cookie(name, value));
    }

    @Override
    public MinijaxTestInvocationBuilder cacheControl(final CacheControl cacheControl) {
        header("Cache-Control", cacheControl);
        return this;
    }

    @Override
    public MinijaxTestInvocationBuilder header(final String name, final Object value) {
        headers.add(name, value.toString());
        return this;
    }

    @Override
    public MinijaxTestInvocationBuilder headers(final MultivaluedMap<String, Object> headers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxTestInvocationBuilder property(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletionStageRxInvoker rx() {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public <T extends RxInvoker> T rx(final Class<T> clazz) {
        throw new UnsupportedOperationException();
    }

    private void setEntity(final Entity<?> entity) {
        if (entity != null && entity.getMediaType() != null) {
            headers.putSingle("Content-Type", entity.getMediaType().toString());
        }
        this.entity = entity;
    }
}
