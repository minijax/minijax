package org.minijax.rs.test;

import java.io.InputStream;
import java.net.URI;

import jakarta.ws.rs.core.UriInfo;

import org.minijax.rs.MinijaxApplicationContext;
import org.minijax.rs.MinijaxHttpHeaders;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.MinijaxUriInfo;

public class MinijaxTestRequestContext extends MinijaxRequestContext {
    private final String method;
    private final UriInfo uriInfo;
    private final MinijaxHttpHeaders httpHeaders;
    private final InputStream entityStream;

    public MinijaxTestRequestContext(
            final MinijaxApplicationContext application,
            final String method,
            final String uri) {
        this(application, method, new MinijaxUriInfo(URI.create(uri)));
    }

    public MinijaxTestRequestContext(
            final MinijaxApplicationContext application,
            final String method,
            final UriInfo uriInfo) {
        this(application, method, uriInfo, new MinijaxTestHttpHeaders(), null);
    }

    public MinijaxTestRequestContext(
            final MinijaxApplicationContext container,
            final String method,
            final UriInfo uriInfo,
            final MinijaxHttpHeaders httpHeaders,
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
    public MinijaxHttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    @Override
    public InputStream getEntityStream() {
        return entityStream;
    }
}
