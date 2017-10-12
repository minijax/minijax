package org.minijax;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.Variant;

public class MinijaxResponseBuilder extends javax.ws.rs.core.Response.ResponseBuilder {
    private final MultivaluedMap<String, Object> headers;
    private final MinijaxStatusInfo statusInfo;
    private Object entity;
    private MediaType mediaType;

    public MinijaxResponseBuilder() {
        headers = new MultivaluedHashMap<>();
        statusInfo = new MinijaxStatusInfo();
    }

    public MinijaxResponseBuilder(final MinijaxResponseBuilder other) {
        headers = new MultivaluedHashMap<>();
        headers.putAll(other.headers);
        statusInfo = new MinijaxStatusInfo();
        statusInfo.setStatusInfo(other.statusInfo);
        entity = other.entity;
        mediaType = other.mediaType;
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
    public Response build() {
        return new MinijaxResponse(this);
    }

    @Override
    public ResponseBuilder clone() {
        return new MinijaxResponseBuilder(this);
    }

    @Override
    public ResponseBuilder status(final int status) {
        // Note: Use setStatusInfo instead of setStatusCode.
        // The former will try to update family and reason phrase for known statuses.
        statusInfo.setStatusInfo(status);
        return this;
    }

    @Override
    public ResponseBuilder status(final int status, final String reasonPhrase) {
        statusInfo.setStatusInfo(status, reasonPhrase);
        return this;
    }

    @Override
    public ResponseBuilder entity(final Object entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public ResponseBuilder entity(final Object entity, final Annotation[] annotations) {
        this.entity = entity;
        return this;
    }

    @Override
    public ResponseBuilder allow(final String... methods) {
        for (final String method : methods) {
            headers.add("Allow", method);
        }
        return this;
    }

    @Override
    public ResponseBuilder allow(final Set<String> methods) {
        for (final String method : methods) {
            headers.add("Allow", method);
        }
        return this;
    }

    @Override
    public ResponseBuilder cacheControl(final CacheControl cacheControl) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder encoding(final String encoding) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder header(final String name, final Object value) {
        headers.add(name, value);
        return this;
    }

    @Override
    public ResponseBuilder replaceAll(final MultivaluedMap<String, Object> headers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder language(final String language) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder language(final Locale language) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder type(final MediaType type) {
        mediaType = type;
        return this;
    }

    @Override
    public ResponseBuilder type(final String type) {
        mediaType = MediaType.valueOf(type);
        return this;
    }

    @Override
    public ResponseBuilder variant(final Variant variant) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder contentLocation(final URI location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder cookie(final NewCookie... cookies) {
        for (final NewCookie cookie : cookies) {
            headers.add("Set-Cookie", cookie);
        }
        return this;
    }

    @Override
    public ResponseBuilder expires(final Date expires) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder lastModified(final Date lastModified) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder location(final URI location) {
        headers.add("Location", location);
        return this;
    }

    @Override
    public ResponseBuilder tag(final EntityTag tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder tag(final String tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder variants(final Variant... variants) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder variants(final List<Variant> variants) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder links(final Link... links) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder link(final URI uri, final String rel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder link(final String uri, final String rel) {
        throw new UnsupportedOperationException();
    }
}
