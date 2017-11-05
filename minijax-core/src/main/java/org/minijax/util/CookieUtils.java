package org.minijax.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Cookie;

public class CookieUtils {

    CookieUtils() {
        throw new UnsupportedOperationException();
    }

    public static javax.servlet.http.Cookie convertJaxToServlet(final javax.ws.rs.core.Cookie input) {
        final javax.servlet.http.Cookie result = new javax.servlet.http.Cookie(input.getName(), input.getValue());

        if (input.getDomain() != null) {
            result.setDomain(input.getDomain());
        }

        result.setPath(input.getPath());
        result.setVersion(input.getVersion());
        result.setSecure(true);
        return result;
    }

    public static javax.servlet.http.Cookie[] convertJaxToServlet(final List<javax.ws.rs.core.Cookie> input) {
        final javax.servlet.http.Cookie[] result = new javax.servlet.http.Cookie[input.size()];

        for (int i = 0; i < input.size(); i++) {
            result[i] = convertJaxToServlet(input.get(i));
        }

        return result;
    }

    public static javax.ws.rs.core.Cookie convertServletToJax(final javax.servlet.http.Cookie sc) {
        return new Cookie(sc.getName(), sc.getValue(), sc.getPath(), sc.getDomain(), sc.getVersion());
    }

    public static Map<String, javax.ws.rs.core.Cookie> convertServletToJax(final javax.servlet.http.Cookie[] input) {
        final Map<String, Cookie> result = new HashMap<>();

        if (input != null) {
            for (final javax.servlet.http.Cookie sc : input) {
                result.put(sc.getName(), convertServletToJax(sc));
            }
        }

        return result;
    }
}
