package org.minijax.commons;

import java.util.Collection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

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

        if (OptionalClasses.ENTITY_MANAGER_FACTORY != null) {
            if (EntityManagerFactory.class.isAssignableFrom(obj.getClass())) {
                closeEntityManagerFactory((EntityManagerFactory) obj);
            } else if (EntityManager.class.isAssignableFrom(obj.getClass())) {
                closeEntityManager((EntityManager) obj);
            }
        }
    }

    private static void closeAutoCloseable(final AutoCloseable obj) {
        try {
            obj.close();
        } catch (final Exception ex) {
            LOG.warn(ex.getMessage(), ex);
        }
    }

    private static void closeEntityManagerFactory(final EntityManagerFactory emf) {
        try {
            if (emf.isOpen()) {
                emf.close();
            }
        } catch (final Exception ex) {
            LOG.warn(ex.getMessage(), ex);
        }
    }

    private static void closeEntityManager(final EntityManager em) {
        try {
            if (em.isOpen()) {
                em.close();
            }
        } catch (final Exception ex) {
            LOG.warn(ex.getMessage(), ex);
        }
    }
}
