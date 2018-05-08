package org.minijax.undertow.websocket;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.minijax.util.LocaleUtils;
import org.minijax.util.MediaTypeUtils;

import io.undertow.websockets.spi.WebSocketHttpExchange;

class MinijaxUndertowWebSocketHttpHeaders implements HttpHeaders {
    private final Map<String, List<String>> headerMap;
    private Map<String, Cookie> cookies;
    private List<Locale> acceptableLanguages;
    private List<MediaType> acceptableMediaTypes;

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
        throw new UnsupportedOperationException();
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

    @Override
    public List<MediaType> getAcceptableMediaTypes() {
        if (acceptableMediaTypes == null) {
            acceptableMediaTypes = MediaTypeUtils.parseMediaTypes(getHeaderString("Accept"));
        }
        return acceptableMediaTypes;
    }

    @Override
    public List<Locale> getAcceptableLanguages() {
        if (acceptableLanguages == null) {
            acceptableLanguages = LocaleUtils.parseAcceptLanguage(getHeaderString("Accept-Language"));
        }
        return acceptableLanguages;
    }

    @Override
    public MediaType getMediaType() {
        final String contentType = getHeaderString("Content-Type");
        return contentType == null ? null : MediaType.valueOf(contentType);
    }

    @Override
    public Locale getLanguage() {
        final String languageTag = getHeaderString("Content-Language");
        return languageTag == null ? null : Locale.forLanguageTag(languageTag);
    }

    @Override
    public int getLength() {
        final String contentLength = getHeaderString("Content-Length");
        if (contentLength == null) {
            return -1;
        }
        try {
            return Integer.parseInt(contentLength);
        } catch (final NumberFormatException ex) {
            return -1;
        }
    }

    @Override
    public Date getDate() {
        throw new UnsupportedOperationException();
    }
}
