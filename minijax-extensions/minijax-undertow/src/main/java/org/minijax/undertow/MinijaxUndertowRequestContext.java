package org.minijax.undertow;

import java.io.InputStream;
import java.net.URI;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import org.minijax.MinijaxApplication;
import org.minijax.MinijaxRequestContext;
import org.minijax.MinijaxUriInfo;

import io.undertow.server.HttpServerExchange;

class MinijaxUndertowRequestContext extends MinijaxRequestContext {
    private final HttpServerExchange exchange;
    private final MinijaxUriInfo uriInfo;
    private MinijaxUndertowHttpHeaders httpHeaders;
    private InputStream entityStream;

    public MinijaxUndertowRequestContext(
            final MinijaxApplication container,
            final HttpServerExchange exchange) {
        super(container);
        this.exchange = exchange;
        uriInfo = new MinijaxUriInfo(URI.create(exchange.getRequestURL()));
    }

    @Override
    public UriInfo getUriInfo() {
        return uriInfo;
    }

    @Override
    public String getMethod() {
        return exchange.getRequestMethod().toString();
    }

    @Override
    public HttpHeaders getHttpHeaders() {
        if (httpHeaders == null) {
            httpHeaders = new MinijaxUndertowHttpHeaders(exchange);
        }
        return httpHeaders;
    }

    @Override
    public InputStream getEntityStream() {
        if (entityStream == null) {
            entityStream = exchange.getInputStream();
        }
        return entityStream;
    }
}
