package org.minijax.nio;

import java.io.InputStream;

import jakarta.ws.rs.core.UriInfo;

import org.minijax.rs.MinijaxApplicationContext;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.MinijaxUriInfo;

class MinijaxNioRequestContext extends MinijaxRequestContext {
    private final String method;
    private final MinijaxUriInfo uriInfo;
    private final MinijaxNioHttpHeaders httpHeaders;
    private final InputStream entityStream;

    public MinijaxNioRequestContext(
            final MinijaxApplicationContext application,
            final String method,
            final MinijaxUriInfo uriInfo,
            final MinijaxNioHttpHeaders httpHeaders,
            final InputStream entityStream) {

        super(application);
        this.method = method;
        this.uriInfo = uriInfo;
        this.httpHeaders = httpHeaders;
        this.entityStream = entityStream;
    }

    @Override
    public UriInfo getUriInfo() {
        return uriInfo;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public MinijaxNioHttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    @Override
    public InputStream getEntityStream() {
        return entityStream;
    }
}
