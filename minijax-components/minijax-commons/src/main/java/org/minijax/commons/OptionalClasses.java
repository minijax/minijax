package org.minijax.commons;

import java.lang.annotation.Annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptionalClasses {
    private static final Logger LOG = LoggerFactory.getLogger(OptionalClasses.class);

    public static final Class<Annotation> SERVER_ENDPOINT = safeGetClass("jakarta.websocket.server.ServerEndpoint");

    public static final Class<?> ENTITY_MANAGER_FACTORY = safeGetClass("jakarta.persistence.EntityManagerFactory");

    public static final Class<?> ENTITY_MANAGER = safeGetClass("jakarta.persistence.EntityManager");

    OptionalClasses() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> safeGetClass(final String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (final ClassNotFoundException ex) {
            LOG.trace(ex.getMessage(), ex);
            return null;
        }
    }
}
