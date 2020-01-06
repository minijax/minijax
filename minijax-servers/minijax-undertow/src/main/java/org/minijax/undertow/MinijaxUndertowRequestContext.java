package org.minijax.undertow;

import java.io.InputStream;
import java.util.Collections;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import org.minijax.rs.MinijaxApplicationContext;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.MinijaxUriInfo;
import org.minijax.rs.uri.MinijaxUriBuilder;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

class MinijaxUndertowRequestContext extends MinijaxRequestContext {
    private final HttpServerExchange exchange;
    private final MinijaxUriInfo uriInfo;
    private MinijaxUndertowHttpHeaders httpHeaders;
    private InputStream entityStream;

    public MinijaxUndertowRequestContext(
            final MinijaxApplicationContext application,
            final HttpServerExchange exchange) {

        super(application);
        this.exchange = exchange;

        final MinijaxUriBuilder uriBuilder = new MinijaxUriBuilder();
        uriBuilder.uri(exchange.getRequestURL());

        final String queryString = exchange.getQueryString();
        if (queryString != null) {
            uriBuilder.replaceQuery(queryString);
        }

        final String forwardedProto = exchange.getRequestHeaders().getFirst(Headers.X_FORWARDED_PROTO);
        if (forwardedProto != null) {
            uriBuilder.scheme(forwardedProto);
        }

        uriInfo = new MinijaxUriInfo(uriBuilder.buildFromMap(Collections.emptyMap()));
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
            httpHeaders = new MinijaxUndertowHttpHeaders(exchange.getRequestHeaders());
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
