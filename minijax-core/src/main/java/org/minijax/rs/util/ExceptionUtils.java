package org.minijax.rs.util;

import jakarta.ws.rs.WebApplicationException;

public class ExceptionUtils {

    ExceptionUtils() {
        throw new UnsupportedOperationException();
    }

    public static WebApplicationException toWebAppException(final Throwable t) {
        if (t instanceof WebApplicationException) {
            return (WebApplicationException) t;
        }
        return new WebApplicationException(t.getMessage(), t);
    }
}
