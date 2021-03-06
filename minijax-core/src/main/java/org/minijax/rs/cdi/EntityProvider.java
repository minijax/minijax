package org.minijax.rs.cdi;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import jakarta.enterprise.inject.InjectionException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;

import org.minijax.cdi.MinijaxProvider;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.util.EntityUtils;

/**
 * Provides the "entity" as defined in section 4.2 of the JAX-RS specification.
 *
 * In short, "entity" refers to the HTTP content body.  The entity provider dispatches
 * to the appropriate <code>MessageBodyReader</code> to parse the HTTP content.
 */
public class EntityProvider<T> implements MinijaxProvider<T> {
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

    @SuppressWarnings("unchecked")
    @Override
    public T get(final Object obj) {
        final MinijaxRequestContext context = (MinijaxRequestContext) obj;

        // Must check for forms BEFORE touching HttpServletRequest.getInputStream().
        // If you call getInputStream -- even if you don't read from it -- the input stream is moved.
        if (entityClass == MultivaluedMap.class) {
            return (T) context.getForm().asForm().asMap();
        }

        final InputStream entityStream = context.getEntityStream();
        try {
            return EntityUtils.readEntity(entityClass, genericType, annotations, mediaType, context, entityStream);
        } catch (final IOException ex) {
            throw new InjectionException(ex.getMessage(), ex);
        }
    }
}
