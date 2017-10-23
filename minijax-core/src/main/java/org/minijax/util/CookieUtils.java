package org.minijax.util;

import java.util.List;

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
}
