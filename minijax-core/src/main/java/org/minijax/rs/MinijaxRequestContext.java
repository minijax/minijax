package org.minijax.rs;

import static javax.ws.rs.core.MediaType.*;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.minijax.commons.IOUtils;
import org.minijax.rs.cdi.ResourceCache;
import org.minijax.rs.multipart.Multipart;

public abstract class MinijaxRequestContext
        implements javax.ws.rs.container.ContainerRequestContext, javax.ws.rs.container.ResourceContext, Closeable {

    private final MinijaxApplicationContext applicationContext;
    private final ResourceCache resourceCache;
    private final Map<String, Object> properties;
    private MinijaxForm form;
    private SecurityContext securityContext;
    private MinijaxResourceMethod resourceMethod;
    private boolean upgraded;

    public MinijaxRequestContext(final MinijaxApplicationContext container) {
        applicationContext = container;
        resourceCache = new ResourceCache();
        properties = new HashMap<>();
    }

    public MinijaxApplicationContext getApplicationContext() {
        return applicationContext;
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


    public abstract HttpHeaders getHttpHeaders();


    /**
     * Get the mutable request headers multivalued map.
     *
     * @return mutable multivalued map of request headers.
     * @see #getHeaderString(String)
     */
    @Override
    public MultivaluedMap<String, String> getHeaders() {
        return getHttpHeaders().getRequestHeaders();
    }


    /**
     * Get any cookies that accompanied the request.
     *
     * @return a read-only map of cookie name (String) to {@link Cookie}.
     */
    @Override
    public Map<String, Cookie> getCookies() {
        return getHttpHeaders().getCookies();
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
        return null;
    }

    @Override
    public void setMethod(final String method) {
        throw new IllegalStateException();
    }

    @Override
    public String getHeaderString(final String name) {
        return getHttpHeaders().getHeaderString(name);
    }

    @Override
    public Date getDate() {
        return getHttpHeaders().getDate();
    }

    @Override
    public Locale getLanguage() {
        return getHttpHeaders().getLanguage();
    }

    @Override
    public int getLength() {
        return getHttpHeaders().getLength();
    }

    @Override
    public MediaType getMediaType() {
        return getHttpHeaders().getMediaType();
    }

    @Override
    public List<MediaType> getAcceptableMediaTypes() {
        return getHttpHeaders().getAcceptableMediaTypes();
    }

    @Override
    public List<Locale> getAcceptableLanguages() {
        return getHttpHeaders().getAcceptableLanguages();
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
        throw new WebApplicationException(response);
    }

    public MinijaxForm getForm() {
        if (form == null) {
            readForm();
        }
        return form;
    }

    private void readForm() {
        final MediaType contentType = getMediaType();

        if (contentType == null) {
            return;
        }

        try {
            if (contentType.isCompatible(APPLICATION_FORM_URLENCODED_TYPE)) {
                form = new MinijaxUrlEncodedForm(IOUtils.toString(getEntityStream(), StandardCharsets.UTF_8));
            } else if (contentType.isCompatible(MULTIPART_FORM_DATA_TYPE)) {
                form = Multipart.read(contentType, getLength(), getEntityStream());
            } else {
                throw new BadRequestException("Unsupported content type (" + contentType + ")");
            }

        } catch (final IOException ex) {
            throw new WebApplicationException(ex.getMessage(), ex);
        }
    }

    public boolean isUpgraded() {
        return upgraded;
    }

    public void setUpgraded(final boolean upgraded) {
        this.upgraded = upgraded;
    }

    @Override
    public void close() throws IOException {
        resourceCache.close();

        if (form != null) {
            form.close();
        }
    }

    public ResourceCache getResourceCache() {
        return resourceCache;
    }

    @Override
    public <T> T getResource(final Class<T> c) {
        return applicationContext.getInjector().getResource(c, this);
    }

    @Override
    public <T> T initResource(final T resource) {
        return applicationContext.getInjector().initResource(resource, this);
    }

    public MinijaxResourceMethod getResourceMethod() {
        return resourceMethod;
    }

    public void setResourceMethod(final MinijaxResourceMethod resourceMethod) {
        this.resourceMethod = resourceMethod;
    }
}
