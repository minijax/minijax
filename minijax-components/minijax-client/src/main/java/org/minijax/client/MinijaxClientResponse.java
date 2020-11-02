package org.minijax.client;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.Link.Builder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.NewCookie;

public class MinijaxClientResponse extends jakarta.ws.rs.core.Response {
    private final HttpResponse<InputStream> innerResponse;
    private StatusType statusInfo;

    public MinijaxClientResponse(final HttpResponse<InputStream> innerResponse) {
        this.innerResponse = Objects.requireNonNull(innerResponse);
    }

    @Override
    public int getStatus() {
        return innerResponse.statusCode();
    }

    @Override
    public StatusType getStatusInfo() {
        if (statusInfo == null) {
            statusInfo = Status.fromStatusCode(innerResponse.statusCode());
        }
        return statusInfo;
    }

    @Override
    public MediaType getMediaType() {
        return MediaType.valueOf(innerResponse.headers().firstValue(HttpHeaders.CONTENT_TYPE).orElse(null));
    }

    @Override
    public Locale getLanguage() {
        return Locale.forLanguageTag(innerResponse.headers().firstValue(HttpHeaders.CONTENT_LANGUAGE).orElse(null));
    }

    @Override
    public int getLength() {
        return Integer.parseInt(innerResponse.headers().firstValue(HttpHeaders.CONTENT_LENGTH).orElse("0"));
    }

    @Override
    public InputStream getEntity() {
        return innerResponse.body();
    }

    @Override
    public <T> T readEntity(final Class<T> entityType) {
        return ConversionUtils.convertToType(getMediaType(), getEntity(), entityType);
    }

    @Override
    public <T> T readEntity(final GenericType<T> entityType) {
        return ConversionUtils.convertToGenericType(getMediaType(), getEntity(), entityType);
    }

    @Override
    public void close() {
        // Nothing to do
    }

    /*
     * Unsupported
     */

    @Override
    public <T> T readEntity(final Class<T> entityType, final Annotation[] annotations) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T readEntity(final GenericType<T> entityType, final Annotation[] annotations) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasEntity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean bufferEntity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getAllowedMethods() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, NewCookie> getCookies() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityTag getEntityTag() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getDate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getLastModified() {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI getLocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Link> getLinks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasLink(final String relation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Link getLink(final String relation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Builder getLinkBuilder(final String relation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultivaluedMap<String, Object> getMetadata() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultivaluedMap<String, String> getStringHeaders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getHeaderString(final String name) {
        throw new UnsupportedOperationException();
    }
}
