package org.minijax.cdi;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import javax.enterprise.inject.InjectionException;
import javax.inject.Provider;
import javax.ws.rs.core.MediaType;

import org.minijax.MinijaxRequestContext;

/**
 * Provides the "entity" as defined in section 4.2 of the JAX-RS specification.
 *
 * In short, "entity" refers to the HTTP content body.  The entity provider dispatches
 * to the appropriate <code>MessageBodyReader</code> to parse the HTTP content.
 */
public class EntityProvider<T> implements Provider<T> {
    private final Class<T> entityClass;
    private final Type genericType;
    private final Annotation[] annotations;
    private final MediaType mediaType;

    public EntityProvider(
            final Class<T> entityClass,
            final Type genericType,
            final Annotation[] annotations,
            final List<MediaType> consumesTypes) {
        this.entityClass = entityClass;
        this.genericType = genericType;
        this.annotations = annotations;
        mediaType = consumesTypes != null && !consumesTypes.isEmpty() ? consumesTypes.get(0) : null;
    }

    @Override
    public T get() {
        final MinijaxRequestContext context = MinijaxRequestContext.getThreadLocal();
        final InputStream entityStream = context.getEntityStream();
        try {
            return context.getApplication().readEntity(entityClass, genericType, annotations, mediaType, context, entityStream);
        } catch (final IOException ex) {
            throw new InjectionException(ex.getMessage(), ex);
        }
    }
}
