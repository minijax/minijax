package org.minijax.test;

import java.io.InputStream;
import java.net.URI;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import org.minijax.MinijaxApplication;
import org.minijax.MinijaxRequestContext;
import org.minijax.MinijaxUriInfo;

public class MinijaxTestRequestContext extends MinijaxRequestContext {
    private final String method;
    private final UriInfo uriInfo;
    private final HttpHeaders httpHeaders;
    private final InputStream entityStream;

    public MinijaxTestRequestContext(
            final MinijaxApplication application,
            final String method,
            final String uri) {
        this(application, method, new MinijaxUriInfo(URI.create(uri)));
    }

    public MinijaxTestRequestContext(
            final MinijaxApplication application,
            final String method,
            final UriInfo uriInfo) {
        this(application, method, uriInfo, new MinijaxTestHttpHeaders(), null);
    }

    public MinijaxTestRequestContext(
            final MinijaxApplication container,
            final String method,
            final UriInfo uriInfo,
            final HttpHeaders httpHeaders,
            final InputStream entityStream) {
        super(container);
        this.method = method;
        this.uriInfo = uriInfo;
        this.httpHeaders = httpHeaders;
        this.entityStream = entityStream;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public UriInfo getUriInfo() {
        return uriInfo;
    }

    @Override
    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    @Override
    public InputStream getEntityStream() {
        return entityStream;
    }
}
