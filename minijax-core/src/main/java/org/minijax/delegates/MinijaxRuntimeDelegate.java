

package org.minijax.delegates;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Link.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Variant.VariantListBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

import org.minijax.MinijaxResponseBuilder;

public class MinijaxRuntimeDelegate extends RuntimeDelegate {

    @Override
    public UriBuilder createUriBuilder() {
        throw new UnsupportedOperationException();
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
            return (HeaderDelegate<T>) new MinijaxMediaTypeDelegate();
        }
        if (type == Cookie.class) {
            return (HeaderDelegate<T>) new MinijaxCookieDelegate();
        }
        if (type == NewCookie.class) {
            return (HeaderDelegate<T>) new MinijaxNewCookieDelegate();
        }
        throw new IllegalArgumentException("Unrecognized header delegate: " + type);
    }

    @Override
    public Builder createLinkBuilder() {
        throw new UnsupportedOperationException();
    }
}
