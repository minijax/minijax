package org.minijax.nio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;

import org.minijax.MinijaxHttpHeaders;

class MinijaxNioHttpHeaders extends MinijaxHttpHeaders {
    private final MultivaluedMap<String, String> requestHeaders;
    private Map<String, Cookie> cookies;

    public MinijaxNioHttpHeaders(final MultivaluedMap<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    @Override
    public MultivaluedMap<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    @Override
    public List<String> getRequestHeader(final String name) {
        return requestHeaders.get(name);
    }

    @Override
    public String getHeaderString(final String name) {
        return requestHeaders.getFirst(name);
    }

    @Override
    public Map<String, Cookie> getCookies() {
        if (cookies == null) {
            cookies = new HashMap<>();
            final List<String> cookieStrList = requestHeaders.get("Cookie");
            if (cookieStrList != null) {
                for (final String cookieStr : cookieStrList) {
                    final int splitIndex = cookieStr.indexOf('=');
                    if (splitIndex >= 0) {
                        final String key = cookieStr.substring(0, splitIndex);
                        final String value = cookieStr.substring(splitIndex + 1);
                        cookies.put(key, new Cookie(key, value));
                    }
                }
            }
        }
        return cookies;
    }
}
