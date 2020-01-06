package org.minijax.undertow;

import java.util.List;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.minijax.rs.MinijaxHttpHeaders;

import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;

class MinijaxUndertowHttpHeaders extends MinijaxHttpHeaders {
    private final HeaderMap headerMap;
    private MultivaluedMap<String, String> requestHeaders;

    public MinijaxUndertowHttpHeaders(final HeaderMap headerMap) {
        this.headerMap = headerMap;
    }

    @Override
    public List<String> getRequestHeader(final String name) {
        return headerMap.get(name);
    }

    @Override
    public String getHeaderString(final String name) {
        return headerMap.getFirst(name);
    }

    @Override
    public MultivaluedMap<String, String> getRequestHeaders() {
        if (requestHeaders == null) {
            requestHeaders = new MultivaluedHashMap<>();
            for (final HttpString name : headerMap.getHeaderNames()) {
                requestHeaders.put(name.toString(), headerMap.get(name));
            }
        }
        return requestHeaders;
    }
}
