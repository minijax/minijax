package org.minijax.util;

import java.lang.annotation.Annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptionalClasses {
    private static final Logger LOG = LoggerFactory.getLogger(OptionalClasses.class);

    public static final Class<?> webSocketUtilsClass = safeGetClass("org.minijax.websocket.MinijaxWebSocketUtils");

    public static final Class<Annotation> serverEndpoint = safeGetClass("javax.websocket.server.ServerEndpoint");


    public static final Class<?> PERSISTENCE_CONTEXT = safeGetClass("javax.persistence.PersistenceContext");


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
