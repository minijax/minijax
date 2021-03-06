
package org.minijax.rs.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

/**
 * The Utils class provides a hodge podge of general utility functions.
 */
public class UrlUtils {

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

        for (final String pair : pairs) {
            final String[] keyValue = pair.split("=");
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

        for (final String pair : pairs) {
            final String[] keyValue = pair.split("=");
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
     * @param str The decoded input string.
     * @return The encoded output string.
     */
    public static String urlEncode(final String str) {
        if (str == null) {
            return "";
        }
        return new UrlEncoder(str, false, false).encode();
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
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }

    /**
     * Encodes a URL but preserves curly brace template syntax.
     *
     * @param str The input URL.
     * @param ignoreTemplates Flag to ignore curly brace templates.
     * @param ignoreSlashes Flag to ignore forward slashes.
     * @return Encoded URL component.
     */
    public static String urlEncode(final String str, final boolean ignoreTemplates, final boolean ignoreSlashes) {
        return new UrlEncoder(str, ignoreTemplates, ignoreSlashes).encode();
    }
}
