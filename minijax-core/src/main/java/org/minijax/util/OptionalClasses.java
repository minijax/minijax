package org.minijax.util;

import java.lang.annotation.Annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptionalClasses {
    private static final Logger LOG = LoggerFactory.getLogger(OptionalClasses.class);

    public static final Class<?> WEB_SOCKET_UTILS = safeGetClass("org.minijax.websocket.MinijaxWebSocketUtils");

    public static final Class<Annotation> SERVER_ENDPOINT = safeGetClass("javax.websocket.server.ServerEndpoint");

    public static final Class<?> ENTITY_MANAGER_FACTORY = safeGetClass("javax.persistence.EntityManagerFactory");

    public static final Class<?> ENTITY_MANAGER = safeGetClass("javax.persistence.EntityManager");

    public static final Class<?> PERSISTENCE_CONTEXT = safeGetClass("javax.persistence.PersistenceContext");

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
