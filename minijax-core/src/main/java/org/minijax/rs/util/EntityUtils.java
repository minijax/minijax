package org.minijax.rs.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.MessageBodyWriter;

import org.minijax.commons.IOUtils;
import org.minijax.commons.MinijaxException;
import org.minijax.rs.MinijaxProviders;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.multipart.Multipart;

public class EntityUtils {

    EntityUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Reads an entity from an entity stream.
     *
     * @param entityClass The result entity class.
     * @param genericType The entity generic type (optional).
     * @param annotations Array of annotations on the type declaration (optional).
     * @param mediaType The HTTP media type.
     * @param context The request context (optional).
     * @param entityStream The entity input stream.
     * @return The entity.
     */
    @SuppressWarnings("unchecked")
    public static <T> T readEntity(
            final Class<T> entityClass,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType,
            final MinijaxRequestContext context,
            final InputStream entityStream)
                    throws IOException {

        if (entityClass == InputStream.class) {
            return (T) entityStream;
        }

        if (entityClass == String.class) {
            return (T) IOUtils.toString(entityStream, StandardCharsets.UTF_8);
        }

        if (context == null) {
            throw new MinijaxException("No application context to read entity type (" + entityClass + ")");
        }

        final MessageBodyReader<T> reader = context.getProviders().getMessageBodyReader(
                entityClass,
                genericType,
                annotations,
                mediaType);

        if (reader == null) {
            throw new MinijaxException("No reader for entity type (" + entityClass + ")");
        }

        return reader.readFrom(
                entityClass,
                genericType,
                annotations,
                mediaType,
                context.getHeaders(),
                entityStream);
    }

    /**
     * Writes an entity to the output stream.
     *
     * @param entity The entity.
     * @param mediaType The entity media type.
     * @param providers Optional providers for custom writers.
     * @param outputStream The output stream.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void writeEntity(
            final Object entity,
            final MediaType mediaType,
            final MinijaxProviders providers,
            final OutputStream outputStream)
                    throws IOException {

        if (entity == null) {
            return;
        }

        if (entity instanceof byte[]) {
            outputStream.write((byte[]) entity);
            return;
        }

        if (entity instanceof InputStream) {
            IOUtils.copy((InputStream) entity, outputStream);
            return;
        }

        if (entity instanceof String) {
            outputStream.write(((String) entity).getBytes(StandardCharsets.UTF_8));
            return;
        }

        if (entity instanceof Form) {
            final String str = UrlUtils.urlEncodeMultivaluedParams(((Form) entity).asMap());
            outputStream.write(str.getBytes(StandardCharsets.UTF_8));
            return;
        }

        if (entity instanceof Multipart) {
            MultipartUtils.serializeMultipartForm((Multipart) entity, outputStream);
            return;
        }

        if (providers != null) {
            final MessageBodyWriter writer = providers.getMessageBodyWriter(entity.getClass(), null, null, mediaType);
            if (writer != null) {
                writer.writeTo(entity, entity.getClass(), null, null, mediaType, null, outputStream);
                return;
            }
        }

        throw new MinijaxException("No writer found for " + entity.getClass() + " and " + mediaType);
    }

    /**
     * Writes an entity to an input stream.
     *
     * This is not used in normal operation of minijax-core.
     * It is used in tests and in minijax-client.
     *
     * @param entity The JAX-RS entity to write.
     * @param providers Optional providers for custom writers.
     * @return An input stream that can be consumed.
     */
    public static <T> InputStream writeEntity(
            final Entity<T> entity,
            final MinijaxProviders providers)
                    throws IOException {

        if (entity == null) {
            return null;
        }

        final T obj = entity.getEntity();
        if (obj == null) {
            return null;
        }

        if (obj instanceof InputStream) {
            return (InputStream) obj;
        }

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        EntityUtils.writeEntity(entity.getEntity(), entity.getMediaType(), providers, outputStream);
        return IOUtils.toInputStream(outputStream.toString(), StandardCharsets.UTF_8);
    }
}
