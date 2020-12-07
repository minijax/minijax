package org.minijax.rs;

import static jakarta.ws.rs.core.MediaType.*;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.ParamConverter;

import org.minijax.commons.IOUtils;
import org.minijax.rs.cdi.ResourceCache;
import org.minijax.rs.delegates.MinijaxResponseBuilder;
import org.minijax.rs.multipart.Multipart;
import org.minijax.rs.util.ExceptionUtils;

public abstract class MinijaxRequestContext
        implements jakarta.ws.rs.container.ContainerRequestContext, jakarta.ws.rs.container.ResourceContext, Closeable {

    private final MinijaxApplicationContext applicationContext;
    private final ResourceCache resourceCache;
    private final Map<String, Object> properties;
    private final MinijaxProviders providers;
    private MinijaxForm form;
    private SecurityContext securityContext;
    private MinijaxResourceMethod resourceMethod;
    private boolean upgraded;

    protected MinijaxRequestContext(final MinijaxApplicationContext container) {
        applicationContext = container;
        resourceCache = new ResourceCache();
        properties = new HashMap<>();
        providers = new MinijaxProviders(this);
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

    public abstract MinijaxHttpHeaders getHttpHeaders();

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
    public MinijaxRequest getRequest() {
        return new MinijaxRequest(getMethod());
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
        throw new MinijaxAbortException(response);
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

    public MinijaxProviders getProviders() {
        return providers;
    }

    public Response toResponse(final Object obj) {
        if (obj == null) {
            throw new NotFoundException();
        }

        if (obj instanceof Response) {
            return (Response) obj;
        }

        return new MinijaxResponseBuilder(this)
                .entity(obj)
                .type(findResponseType(obj, resourceMethod.getProduces()))
                .build();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Response toResponse(final Exception ex) {
        final List<MediaType> mediaTypes;

        if (resourceMethod != null) {
            mediaTypes = resourceMethod.getProduces();
        } else {
            mediaTypes = getAcceptableMediaTypes();
        }

        for (final MediaType mediaType : mediaTypes) {
            final ExceptionMapper mapper = providers.getExceptionMapper(ex.getClass(), mediaType);
            if (mapper != null) {
                return mapper.toResponse(ex);
            }
        }

        return ExceptionUtils.toWebAppException(ex).getResponse();
    }

    @SuppressWarnings("rawtypes")
    private MediaType findResponseType(
            final Object obj,
            final List<MediaType> produces) {

        final Class<?> objType = obj == null ? null : obj.getClass();

        for (final MediaType mediaType : produces) {
            final MessageBodyWriter writer = providers.getMessageBodyWriter(objType, null, null, mediaType);
            if (writer != null) {
                return mediaType;
            }
        }

        return TEXT_PLAIN_TYPE;
    }

    /**
     * Converts a parameter to a type.
     *
     * @param <T>         the supported Java type convertible to/from a {@code String} format.
     * @param str         The parameter string contents.
     * @param c           the raw type of the object to be converted.
     * @param annotations an array of the annotations associated with the convertible
     *                    parameter instance. E.g. if a string value is to be converted into a method parameter,
     *                    this would be the annotations on that parameter as returned by
     *                    {@link java.lang.reflect.Method#getParameterAnnotations}.
     * @return            the newly created instance of {@code T}.
     */
    public <T> T convertParamToType(final String str, final Class<T> c, final Annotation[] annotations) {
        final ParamConverter<T> converter = providers.getParamConverter(c, null, annotations);
        if (converter != null) {
            return converter.fromString(str);
        }
        return null;
    }
}
