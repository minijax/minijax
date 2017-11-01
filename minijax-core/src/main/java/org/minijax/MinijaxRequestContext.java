package org.minijax;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.minijax.cdi.ResourceCache;
import org.minijax.util.IOUtils;
import org.minijax.util.LocaleUtils;
import org.minijax.util.MediaTypeUtils;
import org.minijax.util.UrlUtils;

public class MinijaxRequestContext
        implements javax.ws.rs.container.ContainerRequestContext, Closeable {

    private static final ThreadLocal<MinijaxRequestContext> threadLocalContexts = new ThreadLocal<>();
    private final MinijaxApplication application;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final MinijaxUriInfo uriInfo;
    private final ResourceCache resourceCache;
    private final Map<String, Object> properties;
    private MultivaluedHashMap<String, String> headers;
    private Map<String, Cookie> cookies;
    private MinijaxForm form;
    private List<Locale> acceptableLanguages;
    private List<MediaType> acceptableMediaTypes;
    private SecurityContext securityContext;
    private MinijaxResourceMethod resourceMethod;

    public MinijaxRequestContext(
            final MinijaxApplication container,
            final HttpServletRequest request,
            final HttpServletResponse response) {
        this.application = container;
        this.request = request;
        this.response = response;
        uriInfo = new MinijaxUriInfo(UrlUtils.getFullRequestUrl(request));
        resourceCache = new ResourceCache();
        properties = new HashMap<>();
        threadLocalContexts.set(this);
    }

    public MinijaxApplication getApplication() {
        return application;
    }

    public HttpServletRequest getServletRequest() {
        return request;
    }

    public HttpServletResponse getServletResponse() {
        return response;
    }

    @Override
    public UriInfo getUriInfo() {
        return uriInfo;
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


    /**
     * Get the request method.
     *
     * @return the request method.
     * @see javax.ws.rs.HttpMethod
     */
    @Override
    public String getMethod() {
        return request.getMethod();
    }


    /**
     * Get the mutable request headers multivalued map.
     *
     * @return mutable multivalued map of request headers.
     * @see #getHeaderString(String)
     */
    @Override
    public MultivaluedMap<String, String> getHeaders() {
        if (headers == null) {
            headers = new MultivaluedHashMap<>();

            final Enumeration<String> ne = request.getHeaderNames();
            while (ne.hasMoreElements()) {
                final String name = ne.nextElement();
                final Enumeration<String> ve = request.getHeaders(name);
                while (ve.hasMoreElements()) {
                    headers.add(name, ve.nextElement());
                }
            }
        }
        return headers;
    }


    /**
     * Get any cookies that accompanied the request.
     *
     * @return a read-only map of cookie name (String) to {@link Cookie}.
     */
    @Override
    public Map<String, Cookie> getCookies() {
        if (cookies == null) {
            cookies = new HashMap<>();

            if (request.getCookies() != null) {
                for (final javax.servlet.http.Cookie sc : request.getCookies()) {
                    cookies.put(
                            sc.getName(),
                            new Cookie(sc.getName(), sc.getValue(), sc.getPath(), sc.getDomain(), sc.getVersion()));
                }
            }
        }
        return cookies;
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
            acceptableMediaTypes = MediaTypeUtils.parseMediaTypes(getHeaderString("Accept"));
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

    @Override
    public InputStream getEntityStream() {
        try {
            return request.getInputStream();
        } catch (final IOException ex) {
            throw new MinijaxException(ex);
        }
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
            if (contentType.isCompatible(MediaType.APPLICATION_FORM_URLENCODED_TYPE)) {
                form = new MinijaxUrlEncodedForm(IOUtils.toString(getEntityStream(), StandardCharsets.UTF_8));
            } else if (contentType.isCompatible(MediaType.MULTIPART_FORM_DATA_TYPE)) {
                form = new MinijaxMultipartForm(request.getParts());
            } else {
                throw new BadRequestException("Unsupported content type (" + contentType + ")");
            }

        } catch (final IOException | ServletException ex) {
            throw new WebApplicationException(ex.getMessage(), ex);
        }
    }

    @Override
    public void close() throws IOException {
        resourceCache.close();

        if (form != null) {
            form.close();
        }

        threadLocalContexts.remove();
    }

    public ResourceCache getResourceCache() {
        return resourceCache;
    }

    public MinijaxResourceMethod getResourceMethod() {
        return resourceMethod;
    }

    public void setResourceMethod(final MinijaxResourceMethod resourceMethod) {
        this.resourceMethod = resourceMethod;
    }

    public static MinijaxRequestContext getThreadLocal() {
        final MinijaxRequestContext context = threadLocalContexts.get();
        if (context == null) {
            throw new IllegalStateException("Minijax request context not found");
        }
        return context;
    }
}
