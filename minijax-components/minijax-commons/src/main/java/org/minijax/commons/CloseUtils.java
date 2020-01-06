package org.minijax.commons;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseUtils {
    private static final Logger LOG = LoggerFactory.getLogger(CloseUtils.class);

    CloseUtils() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("rawtypes")
    public static void closeQuietly(final Collection objs) {
        for (final Object obj : objs) {
            closeQuietly(obj);
        }
    }

    public static void closeQuietly(final Object obj) {
        if (obj == null) {
            return;
        }

        if (obj instanceof AutoCloseable) {
            closeAutoCloseable((AutoCloseable) obj);
            return;
        }
    }

    private static void closeAutoCloseable(final AutoCloseable obj) {
        try {
            if (obj != null) {
                obj.close();
            }
        } catch (final Exception ex) {
            LOG.warn(ex.getMessage(), ex);
        }
    }
}
