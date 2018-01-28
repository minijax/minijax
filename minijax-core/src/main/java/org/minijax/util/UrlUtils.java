
package org.minijax.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Utils class provides a hodge podge of general utility functions.
 */
public class UrlUtils {
    private static final Logger LOG = LoggerFactory.getLogger(UrlUtils.class);
    private static final String UTF8 = "UTF-8";
    private static final BitSet URI_WHITELIST_CHARS;

    static {
        URI_WHITELIST_CHARS = new BitSet(256);
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            URI_WHITELIST_CHARS.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            URI_WHITELIST_CHARS.set(i);
        }
        for (i = '0'; i <= '9'; i++) {
            URI_WHITELIST_CHARS.set(i);
        }
        URI_WHITELIST_CHARS.set('-');
        URI_WHITELIST_CHARS.set('_');
        URI_WHITELIST_CHARS.set('.');
        URI_WHITELIST_CHARS.set('*');
    }


    UrlUtils() {
        throw new UnsupportedOperationException();
    }


    public static String concatUrlPaths(final String base, final String path) {
        if (base == null && path == null) {
            return "/";
        }

        String result;

        if (base == null) {
            result = path;
        } else if (path == null) {
            result = base;
        } else {
            result = base + "/" + path;
        }

        result = result.replaceAll("/+", "/"); // Unify slashes

        if (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }

        if (!result.startsWith("/")) {
            result = "/" + result;
        }

        return result;
    }


    /**
     * Returns the full request URL for a servlet request.
     *
     * 1) Fixes HTTPS protocol if forwarded by load balancer.
     * 2) Handles query strings.
     *
     * See:
     * http://stackoverflow.com/a/2222268/2051724
     * http://docs.aws.amazon.com/elasticloadbalancing/latest/classic/x-forwarded-headers.html
     *
     * @param request The original HTTP servlet request.
     * @return The full request URL.
     */
    public static URI getFullRequestUrl(final HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        String url = request.getRequestURL().toString();

        // Fix HTTPS->HTTP rewriting from HTTP proxy.
        // For example, "https://www.ajibot.com" will appear as "http://www.ajibot.com"
        final String forwarded = request.getHeader("X-Forwarded-Proto");
        if (forwarded != null && forwarded.equals("https")) {
            url = url.replaceFirst("http://", "https://");
        }

        final String queryString = request.getQueryString();
        if (queryString != null) {
            url += "?" + queryString;
        }

        return URI.create(url);
    }


    /**
     * Decodes a URL-encoded string into key-value pairs.
     *
     * @param str The input string.
     * @return The key-value pairs.
     */
    public static Map<String, String> urlDecodeParams(final String str) {
        final Map<String, String> fields = new HashMap<>();

        if (str == null || str.isEmpty()) {
            return fields;
        }

        final String[] pairs = str.split("&");

        for (int i = 0; i < pairs.length; i++) {
            final String[] keyValue = pairs[i].split("=");
            final String key = keyValue[0];
            final String value = keyValue.length > 1 ? keyValue[1] : null;
            fields.put(urlDecode(key), urlDecode(value));
        }

        return fields;
    }


    /**
     * Decodes a URL-encoded string into key-value pairs.
     *
     * @param str The input string.
     * @return The key-value pairs.
     */
    public static MultivaluedMap<String, String> urlDecodeMultivaluedParams(final String str) {
        final MultivaluedMap<String, String> fields = new MultivaluedHashMap<>();

        if (str == null || str.isEmpty()) {
            return fields;
        }

        final String[] pairs = str.split("&");

        for (int i = 0; i < pairs.length; i++) {
            final String[] keyValue = pairs[i].split("=");
            final String key = keyValue[0];
            final String value = keyValue.length > 1 ? keyValue[1] : null;
            fields.add(urlDecode(key), urlDecode(value));
        }

        return fields;
    }


    /**
     * Encodes a collection of key-value pairs as a URL encoded string.
     *
     * @param params The parameters.
     * @return The URL encoded string.
     */
    public static String urlEncodeParams(final Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }

        final StringBuilder b = new StringBuilder();

        for (final Entry<String, String> entry : params.entrySet()) {
            if (b.length() > 0) {
                b.append("&");
            }

            b.append(urlEncode(entry.getKey()));
            b.append("=");
            b.append(urlEncode(entry.getValue()));
        }

        return b.toString();
    }


    /**
     * Encodes a collection of key-value pairs as a URL encoded string.
     *
     * @param params The parameters.
     * @return The URL encoded string.
     */
    public static String urlEncodeMultivaluedParams(final MultivaluedMap<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }

        final StringBuilder b = new StringBuilder();

        for (final Entry<String, List<String>> entry : params.entrySet()) {
            for (final String value : entry.getValue()) {
                if (b.length() > 0) {
                    b.append("&");
                }

                b.append(urlEncode(entry.getKey()));
                b.append("=");
                b.append(urlEncode(value));
            }
        }

        return b.toString();
    }


    /**
     * Encodes a string to be used as a URL parameter.
     *
     * Handles spaces.  URLEncoder uses plus, but we want %20.
     * http://stackoverflow.com/questions/4737841/urlencoder-not-able-to-translate-space-character
     *
     * @param str The decoded input string.
     * @return The encoded output string.
     */
    public static String urlEncode(final String str) {
        if (str == null) {
            return "";
        }
        try {
            return URLEncoder.encode(str, UTF8).replaceAll("\\+", "%20");
        } catch (final UnsupportedEncodingException ex) {
            LOG.error("Unsupported Encoding", ex);
            return str;
        }
    }


    /**
     * Decodes a URL encoded string.
     *
     * @param str The encoded input string.
     * @return The decoded output string.
     */
    public static String urlDecode(final String str) {
        if (str == null) {
            return "";
        }
        try {
            return URLDecoder.decode(str, UTF8);
        } catch (final UnsupportedEncodingException ex) {
            LOG.error("Unsupported Encoding", ex);
            return str;
        }
    }


    /**
     * Encodes a URL but preserves curly brace template syntax.
     *
     * @param str The input URL.
     * @return Encoded URL component.
     */
    public static String urlEncodeIgnoreTemplates(final String str) {
        final StringBuilder result = new StringBuilder();
        final byte[] byteArray = str.getBytes(StandardCharsets.UTF_8);
        int curlyDepth = 0;

        for (int i = 0; i < byteArray.length; i++) {
            final int b = byteArray[i] & 0xFF;
            final char c = (char) b;
            if (b == '{') {
                result.append(c);
                curlyDepth++;
            } else if (curlyDepth > 0) {
                result.append(c);
                if (b == '}') {
                    curlyDepth--;
                }
            } else if (b < 256 && URI_WHITELIST_CHARS.get(b)) {
                result.append(c);
            } else if (b >= 0xF0) {
                // 4-byte UTF-8
                escapeBytes(result, byteArray, i, 4);
                i += 3;
            } else if (b >= 0xE0) {
                // 3-byte UTF-8
                escapeBytes(result, byteArray, i, 3);
                i += 2;
            } else if (b >= 0xC0) {
                // 2-byte UTF-8
                escapeBytes(result, byteArray, i, 2);
                i++;
            } else {
                escapeByte(result, b);
            }
        }

        return result.toString();
    }

    private static void escapeBytes(final StringBuilder builder, final byte[] byteArray, final int offset, final int length) {
        for (int i = 0; i < length; i++) {
            escapeByte(builder, byteArray[offset + i]);
        }
    }

    private static void escapeByte(final StringBuilder builder, final int b) {
        builder.append('%');
        builder.append(Character.forDigit((b >> 4) & 0x0F, 16));
        builder.append(Character.forDigit(b & 0x0F, 16));
    }
}
