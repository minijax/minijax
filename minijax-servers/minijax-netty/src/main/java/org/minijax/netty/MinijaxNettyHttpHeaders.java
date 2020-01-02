package org.minijax.netty;

import java.util.List;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.minijax.MinijaxHttpHeaders;

import io.netty.handler.codec.http.HttpRequest;

class MinijaxNettyHttpHeaders extends MinijaxHttpHeaders {
    private final HttpRequest request;
    private MultivaluedMap<String, String> requestHeaders;

    MinijaxNettyHttpHeaders(final HttpRequest request) {
        this.request = request;
    }

    @Override
    public List<String> getRequestHeader(final String name) {
        return request.headers().getAll(name);
    }

    @Override
    public String getHeaderString(final String name) {
        return request.headers().getAsString(name);
    }

    @Override
    public MultivaluedMap<String, String> getRequestHeaders() {
        if (requestHeaders == null) {
            requestHeaders = new MultivaluedHashMap<>();
            request.headers().forEach(entry -> requestHeaders.add(entry.getKey(), entry.getValue()));
        }
        return requestHeaders;
    }
}
