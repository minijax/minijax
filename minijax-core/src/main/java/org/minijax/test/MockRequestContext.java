package org.minijax.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedMap;

import org.minijax.MinijaxRequestContext;
import org.minijax.MinijaxForm;
import org.minijax.MinijaxUrlEncodedForm;

public class MockRequestContext extends MinijaxRequestContext {
    private final String method;
    private final MultivaluedMap<String, String> headers;
    private final Map<String, Cookie> cookies;
    private final Entity<?> entity;

    public MockRequestContext(
            final URI requestUri,
            final String method,
            final MultivaluedMap<String, String> headers,
            final Map<String, Cookie> cookies,
            final Entity<?> entity) {
        super(requestUri);
        this.method = method;
        this.headers = headers;
        this.cookies = cookies;
        this.entity = entity;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public MultivaluedMap<String, String> getHeaders() {
        return headers;
    }

    @Override
    public Map<String, Cookie> getCookies() {
        return cookies;
    }

    @Override
    public InputStream getEntityStream() {
        final Object obj = entity.getEntity();
        if (obj == null) {
            return null;
        }
        if (obj instanceof InputStream) {
            return (InputStream) obj;
        }
        if (obj instanceof String) {
            return new ByteArrayInputStream(((String) obj).getBytes(StandardCharsets.UTF_8));
        }
        throw new IllegalStateException("Entity is not an InputStream (" + obj + ")");
    }

    @Override
    public MinijaxForm getForm() {
        final Object obj = entity.getEntity();
        if (obj == null) {
            return null;
        }
        if (obj instanceof MinijaxForm) {
            return (MinijaxForm) obj;
        }
        if (obj instanceof Form) {
            return new MinijaxUrlEncodedForm(((Form) obj).asMap());
        }
        throw new IllegalStateException("Entity is not a Form (" + obj + ")");
    }
}
