package org.minijax.delegates;

import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;

class MinijaxResponse extends javax.ws.rs.core.Response implements ContainerResponseContext {
    private final MultivaluedMap<String, Object> headers;
    private final MinijaxStatusInfo statusInfo;
    private Map<String, NewCookie> cookies;
    private Date date;
    private Object entity;
    private EntityTag entityTag;
    private Locale language;
    private Date lastModified;
    private int length;
    private Set<Link> links;
    private URI location;
    private MediaType mediaType;

    public MinijaxResponse(final MinijaxResponseBuilder builder) {
        headers = builder.getHeaders();
        statusInfo = new MinijaxStatusInfo(builder.getStatusInfo());
        entity = builder.getEntity();
        mediaType = builder.getMediaType();
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
    public Set<String> getAllowedMethods() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, NewCookie> getCookies() {
        return cookies;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public Object getEntity() {
        return entity;
    }

    @Override
    public Annotation[] getEntityAnnotations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<?> getEntityClass() {
        throw new UnsupportedOperationException();
    }

    @Override
    public OutputStream getEntityStream() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityTag getEntityTag() {
        return entityTag;
    }

    @Override
    public Type getEntityType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultivaluedMap<String, Object> getHeaders() {
        return headers;
    }

    @Override
    public String getHeaderString(final String name) {
        final List<Object> values = headers.get(name);
        return values != null ? values.get(0).toString() : null;
    }

    @Override
    public Locale getLanguage() {
        return language;
    }

    @Override
    public Date getLastModified() {
        return lastModified;
    }

    @Override
    public int getLength() {
        return length;
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
    public Set<Link> getLinks() {
        return links;
    }

    @Override
    public URI getLocation() {
        return location;
    }

    @Override
    public MediaType getMediaType() {
        return mediaType;
    }

    @Override
    public MultivaluedMap<String, Object> getMetadata() {
        throw new UnsupportedOperationException();
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
    public MultivaluedMap<String, String> getStringHeaders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasEntity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasLink(final String relation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T readEntity(final Class<T> entityType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T readEntity(final Class<T> entityType, final Annotation[] annotations) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T readEntity(final GenericType<T> entityType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T readEntity(final GenericType<T> entityType, final Annotation[] annotations) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEntity(final Object entity) {
        this.entity = entity;
    }

    @Override
    public void setEntity(final Object entity, final Annotation[] annotations, final MediaType mediaType) {
        this.entity = entity;
        this.mediaType = mediaType;
    }

    @Override
    public void setEntityStream(final OutputStream outputStream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatus(final int code) {
        // Note: Use setStatusInfo instead of setStatusCode.
        // The former will try to update family and reason phrase for known statuses.
        statusInfo.setStatusInfo(code);
    }

    @Override
    public void setStatusInfo(final StatusType statusInfo) {
        this.statusInfo.setStatusInfo(statusInfo);
    }
}
