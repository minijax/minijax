package org.minijax.undertow.websocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.minijax.MinijaxHttpHeaders;

import io.undertow.websockets.spi.WebSocketHttpExchange;

class MinijaxUndertowWebSocketHttpHeaders extends MinijaxHttpHeaders {
    private final Map<String, List<String>> headerMap;
    private MultivaluedMap<String, String> requestHeaders;
    private Map<String, Cookie> cookies;

    public MinijaxUndertowWebSocketHttpHeaders(final WebSocketHttpExchange exchange) {
        headerMap = exchange.getRequestHeaders();
    }

    @Override
    public List<String> getRequestHeader(final String name) {
        return headerMap.get(name);
    }

    @Override
    public String getHeaderString(final String name) {
        final List<String> values = headerMap.get(name);
        return values == null || values.isEmpty() ? null : values.get(0);
    }

    @Override
    public MultivaluedMap<String, String> getRequestHeaders() {
        if (requestHeaders == null) {
            requestHeaders = new MultivaluedHashMap<>();
            requestHeaders.putAll(headerMap);
        }
        return requestHeaders;
    }

    @Override
    public Map<String, Cookie> getCookies() {
        if (cookies == null) {
            cookies = new HashMap<>();

            final List<String> cookieStrings = headerMap.get("Cookie");
            if (cookieStrings != null) {
                for (final String cookieString : cookieStrings) {
                    final String[] cookieParts = cookieString.split("=", 2);
                    cookies.put(cookieParts[0], new Cookie(cookieParts[0], cookieParts[1]));
                }
            }
        }
        return cookies;
    }
}
