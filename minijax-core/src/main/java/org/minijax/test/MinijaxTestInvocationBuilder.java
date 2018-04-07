package org.minijax.test;

import static javax.ws.rs.HttpMethod.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.CompletionStageRxInvoker;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.RxInvoker;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.minijax.Minijax;
import org.minijax.MinijaxApplication;
import org.minijax.MinijaxRequestContext;
import org.minijax.util.CookieUtils;
import org.minijax.util.EntityUtils;
import org.minijax.util.ExceptionUtils;

public class MinijaxTestInvocationBuilder implements javax.ws.rs.client.Invocation.Builder {
    private final MinijaxTestWebTarget target;
    private final MultivaluedMap<String, String> headers;
    private final List<Cookie> cookies;
    private Entity<?> entity;

    public MinijaxTestInvocationBuilder(final MinijaxTestWebTarget target) {
        this.target = target;
        headers = new MultivaluedHashMap<>();
        cookies = new ArrayList<>();
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

        try {
            final MockHttpServletRequest request = new MockHttpServletRequest(
                    name,
                    target.getUri(),
                    headers,
                    EntityUtils.convertToInputStream(entity),
                    CookieUtils.convertJaxToServlet(cookies));

            final MockHttpServletResponse response = new MockHttpServletResponse();

            try (final MinijaxRequestContext context = new MinijaxRequestContext(application, request, response)) {
                return application.handle(context);
            }
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
        cookies.add(cookie);
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
