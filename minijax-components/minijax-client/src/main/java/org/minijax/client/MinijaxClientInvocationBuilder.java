package org.minijax.client;

import static jakarta.ws.rs.HttpMethod.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import jakarta.ws.rs.client.AsyncInvoker;
import jakarta.ws.rs.client.CompletionStageRxInvoker;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.RxInvoker;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

import org.minijax.commons.MinijaxException;
import org.minijax.rs.util.EntityUtils;

public class MinijaxClientInvocationBuilder implements jakarta.ws.rs.client.Invocation.Builder {
    private static final String TRACE = "TRACE";
    private final MinijaxClient client;
    private final HttpRequest.Builder httpRequest;

    public MinijaxClientInvocationBuilder(final MinijaxClient client, final URI uri) {
        this.client = client;
        httpRequest = HttpRequest.newBuilder().uri(uri);
    }

    HttpRequest.Builder getHttpRequest() {
        return httpRequest;
    }

    @Override
    public MinijaxClientInvocationBuilder header(final String name, final Object value) {
        httpRequest.header(name, value.toString());
        return this;
    }

    @Override
    public MinijaxClientInvocationBuilder headers(final MultivaluedMap<String, Object> headers) {
        for (final Entry<String, List<Object>> entry : headers.entrySet()) {
            for (final Object value : entry.getValue()) {
                httpRequest.header(entry.getKey(), value.toString());
            }
        }
        return this;
    }

    @Override
    public MinijaxClientResponse get() {
        return buildGet().invoke();
    }

    @Override
    public <T> T get(final Class<T> responseType) {
        return buildGet().invoke(responseType);
    }

    @Override
    public <T> T get(final GenericType<T> responseType) {
        return buildGet().invoke(responseType);
    }

    @Override
    public Response put(final Entity<?> entity) {
        return buildPut(entity).invoke();
    }

    @Override
    public <T> T put(final Entity<?> entity, final Class<T> responseType) {
        return buildPut(entity).invoke(responseType);
    }

    @Override
    public <T> T put(final Entity<?> entity, final GenericType<T> responseType) {
        return buildPut(entity).invoke(responseType);
    }

    @Override
    public Response post(final Entity<?> entity) {
        return buildPost(entity).invoke();
    }

    @Override
    public <T> T post(final Entity<?> entity, final Class<T> responseType) {
        return buildPost(entity).invoke(responseType);
    }

    @Override
    public <T> T post(final Entity<?> entity, final GenericType<T> responseType) {
        return buildPost(entity).invoke(responseType);
    }

    @Override
    public Response delete() {
        return buildDelete().invoke();
    }

    @Override
    public <T> T delete(final Class<T> responseType) {
        return buildDelete().invoke(responseType);
    }

    @Override
    public <T> T delete(final GenericType<T> responseType) {
        return buildDelete().invoke(responseType);
    }

    @Override
    public Response head() {
        return build(HEAD).invoke();
    }

    @Override
    public Response options() {
        return build(OPTIONS).invoke();
    }

    @Override
    public <T> T options(final Class<T> responseType) {
        return build(OPTIONS).invoke(responseType);
    }

    @Override
    public <T> T options(final GenericType<T> responseType) {
        return build(OPTIONS).invoke(responseType);
    }

    @Override
    public Response trace() {
        return build(TRACE).invoke();
    }

    @Override
    public <T> T trace(final Class<T> responseType) {
        return build(TRACE).invoke(responseType);
    }

    @Override
    public <T> T trace(final GenericType<T> responseType) {
        return build(TRACE).invoke(responseType);
    }

    @Override
    public Response method(final String name) {
        return build(name).invoke();
    }

    @Override
    public <T> T method(final String name, final Class<T> responseType) {
        return build(name).invoke(responseType);
    }

    @Override
    public <T> T method(final String name, final GenericType<T> responseType) {
        return build(name).invoke(responseType);
    }

    @Override
    public Response method(final String name, final Entity<?> entity) {
        return build(name, entity).invoke();
    }

    @Override
    public <T> T method(final String name, final Entity<?> entity, final Class<T> responseType) {
        return build(name, entity).invoke(responseType);
    }

    @Override
    public <T> T method(final String name, final Entity<?> entity, final GenericType<T> responseType) {
        return build(name, entity).invoke(responseType);
    }

    @Override
    public MinijaxClientInvocation build(final String method) {
        httpRequest.method(method, BodyPublishers.noBody());
        return new MinijaxClientInvocation(client, httpRequest.build());
    }

    @Override
    public MinijaxClientInvocation build(final String method, final Entity<?> entity) {
        final InputStream inputStream;
        try {
            inputStream = EntityUtils.writeEntity(entity, null);
        } catch (final IOException ex) {
            throw new MinijaxException("Error converting entity to input stream: " + ex.getMessage(), ex);
        }

        if (inputStream != null) {
            httpRequest.method(method, BodyPublishers.ofInputStream(() -> inputStream));
        } else {
            httpRequest.method(method, BodyPublishers.noBody());
        }

        return new MinijaxClientInvocation(client, httpRequest.build());
    }

    @Override
    public MinijaxClientInvocation buildGet() {
        return build(GET);
    }

    @Override
    public MinijaxClientInvocation buildDelete() {
        return build(DELETE);
    }

    @Override
    public MinijaxClientInvocation buildPost(final Entity<?> entity) {
        return build(POST, entity);
    }

    @Override
    public MinijaxClientInvocation buildPut(final Entity<?> entity) {
        return build(PUT, entity);
    }

    @Override
    public MinijaxClientInvocationBuilder accept(final String... mediaTypes) {
        return header(HttpHeaders.ACCEPT, String.join(", ", mediaTypes));
    }

    @Override
    public MinijaxClientInvocationBuilder accept(final MediaType... mediaTypes) {
        return header(HttpHeaders.ACCEPT, Arrays.asList(mediaTypes).stream().map(MediaType::toString).collect(Collectors.joining(", ")));
    }

    @Override
    public MinijaxClientInvocationBuilder acceptLanguage(final String... locales) {
        return header(HttpHeaders.ACCEPT_LANGUAGE, String.join(", ", locales));
    }

    @Override
    public MinijaxClientInvocationBuilder acceptLanguage(final Locale... locales) {
        return header(HttpHeaders.ACCEPT_LANGUAGE, Arrays.asList(locales).stream().map(Locale::toLanguageTag).collect(Collectors.joining(", ")));
    }

    @Override
    public MinijaxClientInvocationBuilder acceptEncoding(final String... encodings) {
        return header(HttpHeaders.ACCEPT_ENCODING, String.join(", ", encodings));
    }

    @Override
    public MinijaxClientInvocationBuilder cookie(final Cookie cookie) {
        return header(HttpHeaders.COOKIE, cookie);
    }

    @Override
    public MinijaxClientInvocationBuilder cookie(final String name, final String value) {
        return header(HttpHeaders.COOKIE, new HttpCookie(name, value));
    }

    @Override
    public MinijaxClientInvocationBuilder cacheControl(final CacheControl cacheControl) {
        return header(HttpHeaders.CACHE_CONTROL, cacheControl);
    }

    /*
     * Unsupported
     */

    @Override
    public MinijaxClientInvocationBuilder property(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AsyncInvoker async() {
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
}
