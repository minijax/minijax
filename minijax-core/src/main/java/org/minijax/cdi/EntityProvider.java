package org.minijax.cdi;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.inject.Provider;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import org.minijax.MinijaxRequestContext;
import org.minijax.util.IOUtils;

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
    private final List<MediaType> consumesTypes;

    public EntityProvider(
            final Class<T> entityClass,
            final Type genericType,
            final Annotation[] annotations,
            final List<MediaType> consumesTypes) {
        this.entityClass = entityClass;
        this.genericType = genericType;
        this.annotations = annotations;
        this.consumesTypes = consumesTypes;
    }

    @Override
    public T get() {
        final MinijaxRequestContext context = MinijaxRequestContext.getThreadLocal();
        final InputStream entityStream = context.getEntityStream();
        try {
            return getImpl(context, entityStream);
        } catch (final IOException ex) {
            throw new InjectException(ex.getMessage(), ex);
        }
    }

    @SuppressWarnings("unchecked")
    private T getImpl(final MinijaxRequestContext context, final InputStream entityStream) throws IOException {
        if (entityClass == String.class) {
            return (T) IOUtils.toString(entityStream, StandardCharsets.UTF_8);
        }

        if (entityClass == MultivaluedMap.class) {
            return (T) context.getForm().asForm().asMap();
        }

        final MediaType mediaType = consumesTypes != null && !consumesTypes.isEmpty() ? consumesTypes.get(0) : null;
        final MessageBodyReader<T> reader = context.getApplication().getProviders().getMessageBodyReader(entityClass, genericType, annotations, mediaType);
        if (reader != null) {
            final MultivaluedMap<String, String> httpHeaders = context.getHeaders();
            return reader.readFrom(entityClass, genericType, annotations, mediaType, httpHeaders, entityStream);
        }

        throw new InjectException("Unknown entity type (" + entityClass + ")");
    }
}
