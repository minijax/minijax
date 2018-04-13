package org.minijax.test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.minijax.util.LocaleUtils;
import org.minijax.util.MediaTypeUtils;

public class MinijaxTestHttpHeaders implements HttpHeaders {
    private final MultivaluedMap<String, String> headers;
    private final Map<String, Cookie> cookies;
    private List<Locale> acceptableLanguages;
    private List<MediaType> acceptableMediaTypes;

    public MinijaxTestHttpHeaders() {
        this(new MultivaluedHashMap<>(), new HashMap<>());
    }

    public MinijaxTestHttpHeaders(final MultivaluedMap<String, String> headers, final Map<String, Cookie> cookies) {
        this.headers = headers;
        this.cookies = cookies;
    }

    @Override
    public List<String> getRequestHeader(final String name) {
        return headers.get(name);
    }

    @Override
    public String getHeaderString(final String name) {
        return headers.getFirst(name);
    }

    @Override
    public MultivaluedMap<String, String> getRequestHeaders() {
        return headers;
    }

    @Override
    public Map<String, Cookie> getCookies() {
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
