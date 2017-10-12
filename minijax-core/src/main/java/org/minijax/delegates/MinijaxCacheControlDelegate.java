package org.minijax.delegates;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

public class MinijaxCacheControlDelegate implements HeaderDelegate<CacheControl> {

    @Override
    public CacheControl fromString(final String value) {
        if (value == null) {
            return null;
        }

        final CacheControl result = new CacheControl();
        result.setPrivate(true);
        result.setNoTransform(false);

        for (final String directive : value.split(",\\s+")) {
            if (directive.startsWith("max-age=")) {
                result.setMaxAge(Integer.parseInt(directive.split("=")[1]));
            } else if (directive.equals("must-revalidate")) {
                result.setMustRevalidate(true);
            } else if (directive.equals("no-cache")) {
                result.setNoCache(true);
            } else if (directive.equals("no-store")) {
                result.setNoStore(true);
            } else if (directive.equalsIgnoreCase("no-transform")) {
                result.setNoTransform(true);
            } else if (directive.equals("private")) {
                result.setPrivate(true);
            } else if (directive.equals("proxy-revalidate")) {
                result.setProxyRevalidate(true);
            } else if (directive.equals("public")) {
                result.setPrivate(false);
            } else if (directive.startsWith("s-maxage=")) {
                result.setSMaxAge(Integer.parseInt(directive.split("=")[1]));
            }
        }

        return result;
    }

    @Override
    public String toString(final CacheControl value) {
        if (value == null) {
            return null;
        }

        final StringBuilder b = new StringBuilder();

        if (value.isPrivate()) {
            b.append("private");
        } else {
            b.append("public");
        }

        if (value.getMaxAge() >= 0) {
            b.append(", max-age=" + value.getMaxAge());
        }
        if (value.getSMaxAge() >= 0) {
            b.append(", s-maxage=" + value.getSMaxAge());
        }
        if (value.isMustRevalidate()) {
            b.append(", must-revalidate");
        }
        if (value.isNoCache()) {
            b.append(", no-cache");
        }
        if (value.isNoStore()) {
            b.append(", no-store");
        }
        if (value.isNoTransform()) {
            b.append(", no-transform");
        }
        if (value.isProxyRevalidate()) {
            b.append(", proxy-revalidate");
        }
        return b.toString();
    }
}
