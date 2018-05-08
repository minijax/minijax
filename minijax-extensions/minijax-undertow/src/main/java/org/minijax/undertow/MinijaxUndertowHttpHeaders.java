package org.minijax.undertow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.minijax.MinijaxHttpHeaders;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;

class MinijaxUndertowHttpHeaders extends MinijaxHttpHeaders {
    private final HttpServerExchange exchange;
    private final HeaderMap headerMap;
    private MultivaluedMap<String, String> requestHeaders;
    private Map<String, Cookie> cookies;

    public MinijaxUndertowHttpHeaders(final HttpServerExchange exchange) {
        this.exchange = exchange;
        headerMap = exchange.getRequestHeaders();
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

    @Override
    public Map<String, Cookie> getCookies() {
        if (cookies == null) {
            cookies = new HashMap<>();
            for (final io.undertow.server.handlers.Cookie cookie : exchange.getRequestCookies().values()) {
                cookies.put(cookie.getName(), new Cookie(cookie.getName(), cookie.getValue()));
            }
        }
        return cookies;
    }
}
