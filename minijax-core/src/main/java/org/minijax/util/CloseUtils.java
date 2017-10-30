package org.minijax.util;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseUtils {
    private static final Logger LOG = LoggerFactory.getLogger(CloseUtils.class);

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
            if (obj instanceof EntityManagerFactory) {
                closeEntityManagerFactory((EntityManagerFactory) obj);
                return;
            }

            if (obj instanceof EntityManager) {
                closeEntityManager((EntityManager) obj);
                return;
            }
        }
    }

    public static void closeAutoCloseable(final AutoCloseable obj) {
        try {
            obj.close();
        } catch (final Exception ex) {
            LOG.warn(ex.getMessage(), ex);
        }
    }

    public static void closeEntityManagerFactory(final EntityManagerFactory emf) {
        try {
            emf.close();
        } catch (final Exception ex) {
            LOG.warn(ex.getMessage(), ex);
        }
    }

    public static void closeEntityManager(final EntityManager em) {
        try {
            em.close();
        } catch (final Exception ex) {
            LOG.warn(ex.getMessage(), ex);
        }
    }
}
