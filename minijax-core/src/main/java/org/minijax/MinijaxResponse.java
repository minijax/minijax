package org.minijax;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;

public class MinijaxResponse extends javax.ws.rs.core.Response {
    private final MultivaluedMap<String, Object> headers;
    private final StatusType statusInfo;
    private final Object entity;
    private final MediaType mediaType;
    private Locale language;
    private int length;
    private Map<String, NewCookie> cookies;
    private EntityTag entityTag;
    private Date date;
    private Date lastModified;
    private URI location;
    private Set<Link> links;

    public MinijaxResponse(final MinijaxResponseBuilder builder) {
        headers = builder.getHeaders();
        statusInfo = builder.getStatusInfo();
        entity = builder.getEntity();
        mediaType = builder.getMediaType();
    }

    @Override
    public int getStatus() {
        return statusInfo.getStatusCode();
    }

    @Override
    public StatusType getStatusInfo() {
        return statusInfo;
    }

    @Override
    public Object getEntity() {
        return entity;
    }

    @Override
    public <T> T readEntity(final Class<T> entityType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T readEntity(final GenericType<T> entityType) {
        throw new UnsupportedOperationException();
    }

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
    public void close() {
        // Nothing to do
    }

    @Override
    public MediaType getMediaType() {
        return mediaType;
    }

    @Override
    public Locale getLanguage() {
        return language;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public Set<String> getAllowedMethods() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, NewCookie> getCookies() {
        return cookies;
    }

    @Override
    public EntityTag getEntityTag() {
        return entityTag;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public Date getLastModified() {
        return lastModified;
    }

    @Override
    public URI getLocation() {
        return location;
    }

    @Override
    public Set<Link> getLinks() {
        return links;
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
    public Link.Builder getLinkBuilder(final String relation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultivaluedMap<String, Object> getHeaders() {
        return headers;
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
        return headers.getFirst(name).toString();
    }
}
