//package org.minijax;
//
//import static javax.ws.rs.core.MediaType.*;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URI;
//import java.nio.charset.StandardCharsets;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.BadRequestException;
//import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.core.Cookie;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.MultivaluedMap;
//import javax.ws.rs.core.Request;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.SecurityContext;
//import javax.ws.rs.core.UriInfo;
//
//import org.minijax.cdi.ResourceCache;
//import org.minijax.util.IOUtils;
//import org.minijax.util.UrlUtils;
//
//public class MinijaxServletRequestContext extends MinijaxRequestContext {
//
//    private static final ThreadLocal<MinijaxServletRequestContext> threadLocalContexts = new ThreadLocal<>();
////    private final MinijaxApplication application;
//    private final HttpServletRequest request;
//    private final HttpServletResponse response;
//    private final MinijaxUriInfo uriInfo;
//    private final ResourceCache resourceCache;
//    private final Map<String, Object> properties;
//    private MinijaxHttpHeaders headers;
//    private MinijaxForm form;
//    private SecurityContext securityContext;
//    private MinijaxResourceMethod resourceMethod;
//    private boolean upgraded;
//
//    public MinijaxServletRequestContext(
//            final MinijaxApplication container,
//            final HttpServletRequest request,
//            final HttpServletResponse response) {
////        application = container;
//        super(container);
//        this.request = request;
//        this.response = response;
//        uriInfo = new MinijaxUriInfo(UrlUtils.getFullRequestUrl(request));
//        resourceCache = new ResourceCache();
//        properties = new HashMap<>();
//        threadLocalContexts.set(this);
//    }
//
////    @Override
////    public MinijaxApplication getApplication() {
////        return application;
////    }
//
//    public HttpServletRequest getServletRequest() {
//        return request;
//    }
//
//    public HttpServletResponse getServletResponse() {
//        return response;
//    }
//
//    @Override
//    public UriInfo getUriInfo() {
//        return uriInfo;
//    }
//
//    @Override
//    public Object getProperty(final String name) {
//        return properties.get(name);
//    }
//
//    @Override
//    public Collection<String> getPropertyNames() {
//        return Collections.unmodifiableCollection(properties.keySet());
//    }
//
//    @Override
//    public void setProperty(final String name, final Object object) {
//        properties.put(name, object);
//    }
//
//    @Override
//    public void removeProperty(final String name) {
//        properties.remove(name);
//    }
//
//
//    /**
//     * Get the request method.
//     *
//     * @return the request method.
//     * @see javax.ws.rs.HttpMethod
//     */
//    @Override
//    public String getMethod() {
//        return request.getMethod();
//    }
//
//
//    @Override
//    public MinijaxHttpHeaders getHttpHeaders() {
//        if (headers == null) {
//            headers = new MinijaxHttpHeaders(request);
//        }
//        return headers;
//    }
//
//
//    /**
//     * Get the mutable request headers multivalued map.
//     *
//     * @return mutable multivalued map of request headers.
//     * @see #getHeaderString(String)
//     */
//    @Override
//    public MultivaluedMap<String, String> getHeaders() {
//        return getHttpHeaders().getRequestHeaders();
//    }
//
//
//    /**
//     * Get any cookies that accompanied the request.
//     *
//     * @return a read-only map of cookie name (String) to {@link Cookie}.
//     */
//    @Override
//    public Map<String, Cookie> getCookies() {
//        return getHttpHeaders().getCookies();
//    }
//
//    @Override
//    public void setRequestUri(final URI requestUri) {
//        throw new IllegalStateException();
//    }
//
//    @Override
//    public void setRequestUri(final URI baseUri, final URI requestUri) {
//        throw new IllegalStateException();
//    }
//
//    @Override
//    public Request getRequest() {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public void setMethod(final String method) {
//        throw new IllegalStateException();
//    }
//
//    @Override
//    public String getHeaderString(final String name) {
//        return getHttpHeaders().getHeaderString(name);
//    }
//
//    @Override
//    public Date getDate() {
//        return getHttpHeaders().getDate();
//    }
//
//    @Override
//    public Locale getLanguage() {
//        return getHttpHeaders().getLanguage();
//    }
//
//    @Override
//    public int getLength() {
//        return getHttpHeaders().getLength();
//    }
//
//    @Override
//    public MediaType getMediaType() {
//        return getHttpHeaders().getMediaType();
//    }
//
//    @Override
//    public List<MediaType> getAcceptableMediaTypes() {
//        return getHttpHeaders().getAcceptableMediaTypes();
//    }
//
//    @Override
//    public List<Locale> getAcceptableLanguages() {
//        return getHttpHeaders().getAcceptableLanguages();
//    }
//
//    @Override
//    public boolean hasEntity() {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public void setEntityStream(final InputStream input) {
//        throw new IllegalStateException();
//    }
//
//    @Override
//    public SecurityContext getSecurityContext() {
//        return securityContext;
//    }
//
//    @Override
//    public void setSecurityContext(final SecurityContext context) {
//        securityContext = context;
//    }
//
//    @Override
//    public void abortWith(final Response response) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public InputStream getEntityStream() {
//        try {
//            return request.getInputStream();
//        } catch (final IOException ex) {
//            throw new MinijaxException(ex);
//        }
//    }
//
//    @Override
//    public MinijaxForm getForm() {
//        if (form == null) {
//            readForm();
//        }
//        return form;
//    }
//
//    private void readForm() {
//        final MediaType contentType = getMediaType();
//
//        if (contentType == null) {
//            return;
//        }
//
//        try {
//            if (contentType.isCompatible(APPLICATION_FORM_URLENCODED_TYPE)) {
//                form = new MinijaxUrlEncodedForm(IOUtils.toString(getEntityStream(), StandardCharsets.UTF_8));
//            } else if (contentType.isCompatible(MULTIPART_FORM_DATA_TYPE)) {
//                form = new MinijaxMultipartForm(request.getParts());
//            } else {
//                throw new BadRequestException("Unsupported content type (" + contentType + ")");
//            }
//
//        } catch (final IOException | ServletException ex) {
//            throw new WebApplicationException(ex.getMessage(), ex);
//        }
//    }
//
//    @Override
//    public boolean isUpgraded() {
//        return upgraded;
//    }
//
//    @Override
//    public void setUpgraded(final boolean upgraded) {
//        this.upgraded = upgraded;
//    }
//
//    @Override
//    public void close() throws IOException {
//        resourceCache.close();
//
//        if (form != null) {
//            form.close();
//        }
//
//        threadLocalContexts.remove();
//    }
//
//    @Override
//    public ResourceCache getResourceCache() {
//        return resourceCache;
//    }
//
//    @Override
//    public <T> T get(final Class<T> c) {
//        return getApplication().getResource(c);
//    }
//
//    @Override
//    public MinijaxResourceMethod getResourceMethod() {
//        return resourceMethod;
//    }
//
//    @Override
//    public void setResourceMethod(final MinijaxResourceMethod resourceMethod) {
//        this.resourceMethod = resourceMethod;
//    }
//
//    public static MinijaxServletRequestContext getThreadLocal() {
//        final MinijaxServletRequestContext context = threadLocalContexts.get();
//        if (context == null) {
//            throw new IllegalStateException("Minijax request context not found");
//        }
//        return context;
//    }
//
//    public static MinijaxServletRequestContext tryGetThreadLocal() {
//        return threadLocalContexts.get();
//    }
//}
