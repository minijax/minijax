
package org.minijax.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
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

        final String[] pairs = str.split("\\&");

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

        final String[] pairs = str.split("\\&");

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
}
