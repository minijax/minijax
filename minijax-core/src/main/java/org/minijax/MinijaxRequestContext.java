package org.minijax;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.minijax.util.ClassMap;
import org.minijax.util.LocaleUtils;

public abstract class MinijaxRequestContext
        implements javax.ws.rs.container.ContainerRequestContext, Closeable {

    private static final ThreadLocal<MinijaxRequestContext> threadLocalContexts = new ThreadLocal<>();
    private final MinijaxUriInfo uriInfo;
    private final ClassMap resourceCache;
    private final Map<String, Object> properties;
    private List<Locale> acceptableLanguages;
    private List<MediaType> acceptableMediaTypes;
    private SecurityContext securityContext;
    private MinijaxResourceMethod resourceMethod;


    public MinijaxRequestContext(final URI requestUri) {
        uriInfo = new MinijaxUriInfo(requestUri);
        resourceCache = new ClassMap();
        properties = new HashMap<>();
        threadLocalContexts.set(this);
    }

    @Override
    public Object getProperty(final String name) {
        return properties.get(name);
    }

    @Override
    public Collection<String> getPropertyNames() {
        return Collections.unmodifiableCollection(properties.keySet());
    }

    @Override
    public void setProperty(final String name, final Object object) {
        properties.put(name, object);
    }

    @Override
    public void removeProperty(final String name) {
        properties.remove(name);
    }

    @Override
    public UriInfo getUriInfo() {
        return uriInfo;
    }

    @Override
    public void setRequestUri(final URI requestUri) {
        throw new IllegalStateException();
    }

    @Override
    public void setRequestUri(final URI baseUri, final URI requestUri) {
        throw new IllegalStateException();
    }

    @Override
    public Request getRequest() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMethod(final String method) {
        throw new IllegalStateException();
    }

    @Override
    public String getHeaderString(final String name) {
        final List<String> values = getHeaders().get(name);
        return values == null ? null : String.join(",", values);
    }

    @Override
    public Date getDate() {
        return null;
    }

    @Override
    public Locale getLanguage() {
        final String languageTag = getHeaderString("Content-Language");
        return languageTag == null ? null : Locale.forLanguageTag(languageTag);
    }

    @Override
    public int getLength() {
        final String contentLength = getHeaderString("Content-Length");
        if (contentLength == null) {
            return -1;
        }
        try {
            return Integer.parseInt(contentLength);
        } catch (final NumberFormatException ex) {
            return -1;
        }
    }

    @Override
    public MediaType getMediaType() {
        final String contentType = getHeaderString("Content-Type");
        return contentType == null ? null : MediaType.valueOf(contentType);
    }

    @Override
    public List<MediaType> getAcceptableMediaTypes() {
        if (acceptableMediaTypes == null) {
            acceptableMediaTypes = new ArrayList<>();

            final String accept = getHeaderString("Accept");
            final String[] acceptTypes = accept.split(",\\s+");
            for (final String acceptType : acceptTypes) {
                acceptableMediaTypes.add(MediaType.valueOf(acceptType));
            }
        }
        return acceptableMediaTypes;
    }

    @Override
    public List<Locale> getAcceptableLanguages() {
        if (acceptableLanguages == null) {
            acceptableLanguages = LocaleUtils.parseAcceptLanguage(getHeaderString("Accept-Language"));
        }
        return acceptableLanguages;
    }

    @Override
    public boolean hasEntity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEntityStream(final InputStream input) {
        throw new IllegalStateException();
    }

    @Override
    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    @Override
    public void setSecurityContext(final SecurityContext context) {
        securityContext = context;
    }

    @Override
    public void abortWith(final Response response) {
        throw new UnsupportedOperationException();
    }

    ClassMap getResourceCache() {
        return resourceCache;
    }

    public MinijaxResourceMethod getResourceMethod() {
        return resourceMethod;
    }

    public void setResourceMethod(final MinijaxResourceMethod resourceMethod) {
        this.resourceMethod = resourceMethod;
    }

    public abstract MinijaxForm getForm();

    @Override
    public void close() throws IOException {
        threadLocalContexts.remove();
    }

    public static MinijaxRequestContext getThreadLocal() {
        return threadLocalContexts.get();
    }
}
