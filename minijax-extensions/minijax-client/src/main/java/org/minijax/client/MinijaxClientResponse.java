package org.minijax.client;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Link.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.minijax.delegates.MinijaxStatusInfo;

public class MinijaxClientResponse extends javax.ws.rs.core.Response {
    private final CloseableHttpResponse innerResponse;
    private MinijaxStatusInfo statusInfo;

    public MinijaxClientResponse(final CloseableHttpResponse innerResponse) {
        this.innerResponse = innerResponse;
    }

    @Override
    public int getStatus() {
        return innerResponse.getStatusLine().getStatusCode();
    }

    @Override
    public StatusType getStatusInfo() {
        if (statusInfo == null) {
            statusInfo = new MinijaxStatusInfo();
            statusInfo.setStatusCode(innerResponse.getStatusLine().getStatusCode());
            statusInfo.setReasonPhrase(innerResponse.getStatusLine().getReasonPhrase());
        }
        return statusInfo;
    }

    @Override
    public Object getEntity() {
        return innerResponse.getEntity();
    }

    @Override
    public <T> T readEntity(final Class<T> entityType) {
        return ConversionUtils.convertApacheToJax(innerResponse.getEntity(), entityType);
    }

    @Override
    public <T> T readEntity(final GenericType<T> entityType) {
        return ConversionUtils.convertToGenericType(innerResponse.getEntity(), entityType);
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
        try {
            innerResponse.close();
        } catch (final IOException ex) {
            throw new WebApplicationException(ex);
        }
    }

    @Override
    public MediaType getMediaType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale getLanguage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLength() {
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
