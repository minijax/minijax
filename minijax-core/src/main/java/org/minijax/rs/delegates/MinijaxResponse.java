package org.minijax.rs.delegates;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;

import org.minijax.rs.MinijaxApplicationContext;
import org.minijax.rs.MinijaxException;
import org.minijax.rs.util.EntityUtils;

class MinijaxResponse extends javax.ws.rs.core.Response implements ContainerResponseContext {
    private final MinijaxApplicationContext application;
    private final MultivaluedMap<String, Object> headers;
    private final MinijaxStatusInfo statusInfo;
    private Date date;
    private Object entity;
    private Locale language;
    private Date lastModified;
    private int length;
    private Set<Link> links;
    private MediaType mediaType;

    public MinijaxResponse(final MinijaxResponseBuilder builder) {
        application = builder.getApplicationContext();
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
        final Map<String, NewCookie> result = new HashMap<>();
        final List<Object> values = headers.get(HttpHeaders.SET_COOKIE);
        if (values != null) {
            for (final Object value : values) {
                final NewCookie cookie = NewCookie.valueOf(value.toString());
                result.put(cookie.getName(), cookie);
            }
        }
        return result;
    }

    @Override
    public Date getDate() {
        return date;
    }

    /**
     * Returns the entity as provided by the resource method.
     */
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
        return entity == null ? null : entity.getClass();
    }

    @Override
    public OutputStream getEntityStream() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityTag getEntityTag() {
        throw new UnsupportedOperationException();
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
        final String locationStr = getHeaderString(HttpHeaders.LOCATION);
        return locationStr == null ? null : URI.create(locationStr);
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

    /**
     * Reads the entity as the specified type.
     *
     * The type may be one of the following:
     *   1) The original entity type, in which case the original entity will be returned directly.
     *   2) A string, in which case the entity will be serialized using the normal serialization flow.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T readEntity(final Class<T> entityType) {
        if (entity == null) {
            return null;
        }

        if (entityType.isAssignableFrom(entity.getClass())) {
            return (T) entity;
        }

        if (entityType != String.class) {
            throw new IllegalArgumentException("Unsupported entity type (" + entityType + ")");
        }

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            EntityUtils.writeEntity(entity, null, application, outputStream);
            return (T) outputStream.toString();
        } catch (final IOException ex) {
             throw new MinijaxException(ex.getMessage(), ex);
        }
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
    public void setEntityStream(final OutputStream entityStream) {
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
