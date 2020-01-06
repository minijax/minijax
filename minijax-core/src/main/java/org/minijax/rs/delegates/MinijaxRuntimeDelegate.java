package org.minijax.rs.delegates;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Link.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Variant.VariantListBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

import org.minijax.rs.uri.MinijaxUriBuilder;

public class MinijaxRuntimeDelegate extends RuntimeDelegate {
    private static final MinijaxMediaTypeDelegate MEDIA_TYPE_DELEGATE = new MinijaxMediaTypeDelegate();
    private static final MinijaxCookieDelegate COOKIE_DELEGATE = new MinijaxCookieDelegate();
    private static final MinijaxNewCookieDelegate NEW_COOKIE_DELEGATE = new MinijaxNewCookieDelegate();
    private static final MinijaxCacheControlDelegate CACHE_CONTROL_DELEGATE = new MinijaxCacheControlDelegate();

    @Override
    public UriBuilder createUriBuilder() {
        return new MinijaxUriBuilder();
    }

    @Override
    public ResponseBuilder createResponseBuilder() {
        return new MinijaxResponseBuilder();
    }

    @Override
    public VariantListBuilder createVariantListBuilder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T createEndpoint(final Application application, final Class<T> endpointType) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> HeaderDelegate<T> createHeaderDelegate(final Class<T> type) {
        if (type == MediaType.class) {
            return (HeaderDelegate<T>) MEDIA_TYPE_DELEGATE;
        }
        if (type == Cookie.class) {
            return (HeaderDelegate<T>) COOKIE_DELEGATE;
        }
        if (type == NewCookie.class) {
            return (HeaderDelegate<T>) NEW_COOKIE_DELEGATE;
        }
        if (type == CacheControl.class) {
            return (HeaderDelegate<T>) CACHE_CONTROL_DELEGATE;
        }
        throw new IllegalArgumentException("Unrecognized header delegate: " + type);
    }

    @Override
    public Builder createLinkBuilder() {
        throw new UnsupportedOperationException();
    }
}
