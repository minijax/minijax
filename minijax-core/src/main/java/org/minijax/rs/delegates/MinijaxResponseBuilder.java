package org.minijax.rs.delegates;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response.StatusType;
import jakarta.ws.rs.core.Variant;

import org.minijax.rs.MinijaxRequestContext;

public class MinijaxResponseBuilder extends jakarta.ws.rs.core.Response.ResponseBuilder {
    private final MinijaxRequestContext context;
    private final MultivaluedMap<String, Object> headers;
    private final MinijaxStatusInfo statusInfo;
    private Object entity;
    private MediaType mediaType;

    public MinijaxResponseBuilder() {
        context = null;
        headers = new MultivaluedHashMap<>();
        statusInfo = new MinijaxStatusInfo();
    }

    public MinijaxResponseBuilder(final MinijaxRequestContext context) {
        this.context = context;
        headers = new MultivaluedHashMap<>();
        statusInfo = new MinijaxStatusInfo();
    }

    private MinijaxResponseBuilder(final MinijaxResponseBuilder other) {
        context = other.context;
        headers = new MultivaluedHashMap<>();
        headers.putAll(other.headers);
        statusInfo = new MinijaxStatusInfo();
        statusInfo.setStatusInfo(other.statusInfo);
        entity = other.entity;
        mediaType = other.mediaType;
    }

    public MinijaxRequestContext getContext() {
        return context;
    }

    public MultivaluedMap<String, Object> getHeaders() {
        return headers;
    }

    public StatusType getStatusInfo() {
        return statusInfo;
    }

    public Object getEntity() {
        return entity;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    @Override
    public MinijaxResponse build() {
        return new MinijaxResponse(this);
    }

    @Override
    @SuppressWarnings({ "squid:S2975", "squid:S1182" })
    public MinijaxResponseBuilder clone() {
        return new MinijaxResponseBuilder(this);
    }

    @Override
    public MinijaxResponseBuilder status(final int status) {
        // Note: Use setStatusInfo instead of setStatusCode.
        // The former will try to update family and reason phrase for known statuses.
        statusInfo.setStatusInfo(status);
        return this;
    }

    @Override
    public MinijaxResponseBuilder status(final int status, final String reasonPhrase) {
        statusInfo.setStatusInfo(status, reasonPhrase);
        return this;
    }

    @Override
    public MinijaxResponseBuilder entity(final Object entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public MinijaxResponseBuilder entity(final Object entity, final Annotation[] annotations) {
        this.entity = entity;
        return this;
    }

    @Override
    public MinijaxResponseBuilder allow(final String... methods) {
        for (final String method : methods) {
            headers.add("Allow", method);
        }
        return this;
    }

    @Override
    public MinijaxResponseBuilder allow(final Set<String> methods) {
        for (final String method : methods) {
            headers.add("Allow", method);
        }
        return this;
    }

    @Override
    public MinijaxResponseBuilder cacheControl(final CacheControl cacheControl) {
        header("Cache-Control", cacheControl);
        return this;
    }

    @Override
    public MinijaxResponseBuilder encoding(final String encoding) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxResponseBuilder header(final String name, final Object value) {
        headers.add(name, value);
        return this;
    }

    @Override
    public MinijaxResponseBuilder replaceAll(final MultivaluedMap<String, Object> headers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxResponseBuilder language(final String language) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxResponseBuilder language(final Locale language) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxResponseBuilder type(final MediaType type) {
        mediaType = type;
        return this;
    }

    @Override
    public MinijaxResponseBuilder type(final String type) {
        mediaType = MediaType.valueOf(type);
        return this;
    }

    @Override
    public MinijaxResponseBuilder variant(final Variant variant) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxResponseBuilder contentLocation(final URI location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxResponseBuilder cookie(final NewCookie... cookies) {
        for (final NewCookie cookie : cookies) {
            headers.add("Set-Cookie", cookie);
        }
        return this;
    }

    @Override
    public MinijaxResponseBuilder expires(final Date expires) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxResponseBuilder lastModified(final Date lastModified) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxResponseBuilder location(final URI location) {
        headers.add("Location", location);
        return this;
    }

    @Override
    public MinijaxResponseBuilder tag(final EntityTag tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxResponseBuilder tag(final String tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxResponseBuilder variants(final Variant... variants) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxResponseBuilder variants(final List<Variant> variants) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxResponseBuilder links(final Link... links) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxResponseBuilder link(final URI uri, final String rel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxResponseBuilder link(final String uri, final String rel) {
        throw new UnsupportedOperationException();
    }
}
