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

import javax.ws.rs.container.PreMatching;
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


    /**
     * Returns the property with the given name registered in the current request/response
     * exchange context, or {@code null} if there is no property by that name.
     * <p>
     * A property allows a JAX-RS filters and interceptors to exchange
     * additional custom information not already provided by this interface.
     * </p>
     * <p>
     * A list of supported properties can be retrieved using {@link #getPropertyNames()}.
     * Custom property names should follow the same convention as package names.
     * </p>
     * <p>
     * In a Servlet container, the properties are synchronized with the {@code ServletRequest}
     * and expose all the attributes available in the {@code ServletRequest}. Any modifications
     * of the properties are also reflected in the set of properties of the associated
     * {@code ServletRequest}.
     * </p>
     *
     * @param name a {@code String} specifying the name of the property.
     * @return an {@code Object} containing the value of the property, or
     *         {@code null} if no property exists matching the given name.
     * @see #getPropertyNames()
     */
    @Override
    public Object getProperty(final String name) {
        return properties.get(name);
    }


    /**
     * Returns an immutable {@link java.util.Collection collection} containing the property
     * names available within the context of the current request/response exchange context.
     * <p>
     * Use the {@link #getProperty} method with a property name to get the value of
     * a property.
     * </p>
     * <p>
     * In a Servlet container, the properties are synchronized with the {@code ServletRequest}
     * and expose all the attributes available in the {@code ServletRequest}. Any modifications
     * of the properties are also reflected in the set of properties of the associated
     * {@code ServletRequest}.
     * </p>
     *
     * @return an immutable {@link java.util.Collection collection} of property names.
     * @see #getProperty
     */
    @Override
    public Collection<String> getPropertyNames() {
        return Collections.unmodifiableCollection(properties.keySet());
    }


    /**
     * Binds an object to a given property name in the current request/response
     * exchange context. If the name specified is already used for a property,
     * this method will replace the value of the property with the new value.
     * <p>
     * A property allows a JAX-RS filters and interceptors to exchange
     * additional custom information not already provided by this interface.
     * </p>
     * <p>
     * A list of supported properties can be retrieved using {@link #getPropertyNames()}.
     * Custom property names should follow the same convention as package names.
     * </p>
     * <p>
     * If a {@code null} value is passed, the effect is the same as calling the
     * {@link #removeProperty(String)} method.
     * </p>
     * <p>
     * In a Servlet container, the properties are synchronized with the {@code ServletRequest}
     * and expose all the attributes available in the {@code ServletRequest}. Any modifications
     * of the properties are also reflected in the set of properties of the associated
     * {@code ServletRequest}.
     * </p>
     *
     * @param name   a {@code String} specifying the name of the property.
     * @param object an {@code Object} representing the property to be bound.
     */
    @Override
    public void setProperty(final String name, final Object object) {
        properties.put(name, object);
    }


    /**
     * Removes a property with the given name from the current request/response
     * exchange context. After removal, subsequent calls to {@link #getProperty}
     * to retrieve the property value will return {@code null}.
     * <p>
     * In a Servlet container, the properties are synchronized with the {@code ServletRequest}
     * and expose all the attributes available in the {@code ServletRequest}. Any modifications
     * of the properties are also reflected in the set of properties of the associated
     * {@code ServletRequest}.
     * </p>
     *
     * @param name a {@code String} specifying the name of the property to be removed.
     */
    @Override
    public void removeProperty(final String name) {
        properties.remove(name);
    }


    /**
     * Get request URI information.
     *
     * The returned object contains "live" view of the request URI information in
     * a sense that any changes made to the request URI using one of the
     * {@code setRequestUri(...)} methods will be reflected in the previously
     * returned {@link MinijaxUriInfo} instance.
     *
     * @return request URI information.
     */
    @Override
    public UriInfo getUriInfo() {
        return uriInfo;
    }


    /**
     * Set a new request URI using the current base URI of the application to
     * resolve the application-specific request URI part.
     * <p>
     * Note that the method is usable only in pre-matching filters, prior to the resource
     * matching occurs. Trying to invoke the method in a filter bound to a resource method
     * results in an {@link IllegalStateException} being thrown.
     * </p>
     *
     * @param requestUri new URI of the request.
     * @throws IllegalStateException in case the method is not invoked from a {@link PreMatching pre-matching}
     *                               request filter.
     * @see #setRequestUri(java.net.URI, java.net.URI)
     */
    @Override
    public void setRequestUri(final URI requestUri) {
        throw new IllegalStateException();
    }


    /**
     * Set a new request URI using a new base URI to resolve the application-specific
     * request URI part.
     * <p>
     * Note that the method is usable only in pre-matching filters, prior to the resource
     * matching occurs. Trying to invoke the method in a filter bound to a resource method
     * results in an {@link IllegalStateException} being thrown.
     * </p>
     *
     * @param baseUri    base URI that will be used to resolve the application-specific
     *                   part of the request URI.
     * @param requestUri new URI of the request.
     * @throws IllegalStateException in case the method is not invoked from a {@link PreMatching pre-matching}
     *                               request filter.
     * @see #setRequestUri(java.net.URI)
     */
    @Override
    public void setRequestUri(final URI baseUri, final URI requestUri) {
        throw new IllegalStateException();
    }


    /**
     * Get the injectable request information.
     *
     * @return injectable request information.
     */
    @Override
    public Request getRequest() {
        throw new UnsupportedOperationException();
    }


    /**
     * Set the request method.
     * <p>
     * Note that the method is usable only in pre-matching filters, prior to the resource
     * matching occurs. Trying to invoke the method in a filter bound to a resource method
     * results in an {@link IllegalStateException} being thrown.
     * </p>
     *
     * @param method new request method.
     * @throws IllegalStateException in case the method is not invoked from a {@link PreMatching pre-matching}
     *                               request filter.
     * @see javax.ws.rs.HttpMethod
     */
    @Override
    public void setMethod(final String method) {
        throw new IllegalStateException();
    }


    /**
     * Get a message header as a single string value.
     *
     * @param name the message header.
     * @return the message header value. If the message header is not present then
     *         {@code null} is returned. If the message header is present but has no
     *         value then the empty string is returned. If the message header is present
     *         more than once then the values of joined together and separated by a ','
     *         character.
     * @see #getHeaders()
     */
    @Override
    public String getHeaderString(final String name) {
        final List<String> values = getHeaders().get(name);
        return values == null ? null : String.join(",", values);
    }


    /**
     * Get message date.
     *
     * @return the message date, otherwise {@code null} if not present.
     */
    @Override
    public Date getDate() {
        return null;
    }


    /**
     * Get the language of the entity.
     *
     * @return the language of the entity or {@code null} if not specified
     */
    @Override
    public Locale getLanguage() {
        final String languageTag = getHeaderString("Content-Language");
        return languageTag == null ? null : Locale.forLanguageTag(languageTag);
    }


    /**
     * Get Content-Length value.
     *
     * @return Content-Length as integer if present and valid number. In other
     *         cases returns {@code -1}.
     */
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


    /**
     * Get the media type of the entity.
     *
     * @return the media type or {@code null} if not specified (e.g. there's no
     *         request entity).
     */
    @Override
    public MediaType getMediaType() {
        final String contentType = getHeaderString("Content-Type");
        return contentType == null ? null : MediaType.valueOf(contentType);
    }


    /**
     * Get a list of media types that are acceptable for the response.
     *
     * @return a read-only list of requested response media types sorted according
     *         to their q-value, with highest preference first.
     */
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


    /**
     * Get a list of languages that are acceptable for the response.
     *
     * @return a read-only list of acceptable languages sorted according
     *         to their q-value, with highest preference first.
     */
    @Override
    public List<Locale> getAcceptableLanguages() {
        if (acceptableLanguages == null) {
            acceptableLanguages = LocaleUtils.parseAcceptLanguage(getHeaderString("Accept-Language"));
        }
        return acceptableLanguages;
    }


    /**
     * Check if there is a non-empty entity input stream  available in the request
     * message.
     *
     * The method returns {@code true} if the entity is present, returns
     * {@code false} otherwise.
     *
     * @return {@code true} if there is an entity present in the message,
     *         {@code false} otherwise.
     */
    @Override
    public boolean hasEntity() {
        throw new UnsupportedOperationException();
    }


    /**
     * Set a new entity input stream. The JAX-RS runtime is responsible for
     * closing the input stream.
     *
     * @param input new entity input stream.
     * @throws IllegalStateException in case the method is invoked from a response filter.
     */
    @Override
    public void setEntityStream(final InputStream input) {
        throw new IllegalStateException();
    }


    /**
     * Get the injectable security context information for the current request.
     *
     * The {@link SecurityContext#getUserPrincipal()} must return {@code null}
     * if the current request has not been authenticated.
     *
     * @return injectable request security context information.
     */
    @Override
    public SecurityContext getSecurityContext() {
        return securityContext;
    }


    /**
     * Set a new injectable security context information for the current request.
     *
     * The {@link SecurityContext#getUserPrincipal()} must return {@code null}
     * if the current request has not been authenticated.
     *
     * @param context new injectable request security context information.
     * @throws IllegalStateException in case the method is invoked from a response filter.
     */
    @Override
    public void setSecurityContext(final SecurityContext context) {
        securityContext = context;
    }


    /**
     * Abort the filter chain with a response.
     *
     * This method breaks the filter chain processing and returns the provided
     * response back to the client. The provided response goes through the
     * chain of applicable response filters.
     *
     * @param response response to be sent back to the client.
     * @throws IllegalStateException in case the method is invoked from a response filter.
     */
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


    /**
     * Returns the entity as form content.
     *
     * @return The form.
     */
    public abstract MinijaxForm getForm();


    @Override
    public void close() throws IOException {
        threadLocalContexts.remove();
    }


    public static MinijaxRequestContext getThreadLocal() {
        return threadLocalContexts.get();
    }
}
