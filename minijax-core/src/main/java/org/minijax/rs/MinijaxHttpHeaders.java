package org.minijax.rs;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

import org.minijax.rs.util.LocaleUtils;
import org.minijax.rs.util.MediaTypeUtils;

public abstract class MinijaxHttpHeaders implements HttpHeaders {
    private List<Locale> acceptableLanguages;
    private List<MediaType> acceptableMediaTypes;
    private Map<String, Cookie> cookies;

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

    @Override
    public Map<String, Cookie> getCookies() {
        if (cookies == null) {
            buildCookies();
        }
        return cookies;
    }

    private void buildCookies() {
        cookies = new HashMap<>();

        final List<String> cookieStrList = getRequestHeader("Cookie");
        if (cookieStrList == null) {
            return;
        }

        for (final String line : cookieStrList) {
            for (final String chunk : line.split(";")) {
                final String[] parts = chunk.split("=", 2);
                final String key = parts[0].trim();
                String value = parts[1].trim();
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                cookies.put(key, new Cookie(key, value));
            }
        }
    }
}
