package org.minijax.undertow.websocket;

import java.io.InputStream;
import java.net.URI;

import jakarta.ws.rs.core.UriInfo;

import org.minijax.rs.MinijaxApplication;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.MinijaxUriInfo;

import io.undertow.websockets.spi.WebSocketHttpExchange;

class MinijaxUndertowWebSocketRequestContext extends MinijaxRequestContext {
    private final WebSocketHttpExchange exchange;
    private final MinijaxUriInfo uriInfo;
    private MinijaxUndertowWebSocketHttpHeaders httpHeaders;

    public MinijaxUndertowWebSocketRequestContext(
            final MinijaxApplication container,
            final WebSocketHttpExchange exchange) {
        super(container);
        this.exchange = exchange;
        uriInfo = new MinijaxUriInfo(URI.create(exchange.getRequestURI() + "?" + exchange.getQueryString()));
    }

    @Override
    public UriInfo getUriInfo() {
        return uriInfo;
    }

    @Override
    public String getMethod() {
        return "GET";
    }

    @Override
    public MinijaxUndertowWebSocketHttpHeaders getHttpHeaders() {
        if (httpHeaders == null) {
            httpHeaders = new MinijaxUndertowWebSocketHttpHeaders(exchange);
        }
        return httpHeaders;
    }

    @Override
    public InputStream getEntityStream() {
        return null;
    }
}
