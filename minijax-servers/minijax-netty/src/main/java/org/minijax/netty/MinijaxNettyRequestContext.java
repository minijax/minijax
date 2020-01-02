package org.minijax.netty;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import org.minijax.MinijaxApplicationContext;
import org.minijax.MinijaxRequestContext;
import org.minijax.MinijaxUriInfo;
import org.minijax.uri.MinijaxUriBuilder;

import io.netty.handler.codec.http.FullHttpRequest;

class MinijaxNettyRequestContext extends MinijaxRequestContext {
    private final FullHttpRequest request;
    private final MinijaxUriInfo uriInfo;
    private MinijaxNettyHttpHeaders httpHeaders;
    private InputStream entityStream;

    MinijaxNettyRequestContext(
            final MinijaxApplicationContext application,
            final FullHttpRequest request) {

        super(application);
        this.request = request;

        final MinijaxUriBuilder uriBuilder = new MinijaxUriBuilder();
        uriBuilder.uri(request.uri());
        uriInfo = new MinijaxUriInfo(uriBuilder.buildFromMap(Collections.emptyMap()));
    }

    @Override
    public UriInfo getUriInfo() {
        return uriInfo;
    }

    @Override
    public String getMethod() {
        return request.method().name();
    }

    @Override
    public HttpHeaders getHttpHeaders() {
        if (httpHeaders == null) {
            httpHeaders = new MinijaxNettyHttpHeaders(request);
        }
        return httpHeaders;
    }

    @Override
    public InputStream getEntityStream() {
        if (entityStream == null) {
            entityStream = new ByteArrayInputStream(request.content().array());
        }
        return entityStream;
    }
}
