package org.minijax.util;

import static javax.ws.rs.core.MediaType.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import javax.enterprise.inject.InjectionException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import org.minijax.MinijaxApplicationContext;
import org.minijax.MinijaxRequestContext;
import org.minijax.multipart.Multipart;

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

        if (entityClass == MultivaluedMap.class) {
            return (T) context.getForm().asForm().asMap();
        }

        if (context != null) {
            final MinijaxApplicationContext appCtx = context.getApplicationContext();
            final MessageBodyReader<T> reader = appCtx.getProviders().getMessageBodyReader(
                    entityClass,
                    genericType,
                    annotations,
                    mediaType);
            if (reader != null) {
                return reader.readFrom(
                        entityClass,
                        genericType,
                        annotations,
                        mediaType,
                        context.getHeaders(),
                        entityStream);
            }
        }

        throw new InjectionException("Unknown entity type (" + entityClass + ")");
    }

    public static InputStream convertToInputStream(final Entity<?> entity) throws IOException {
        if (entity == null || entity.getEntity() == null) {
            return null;
        }

        final Object obj = entity.getEntity();

        if (obj instanceof InputStream) {
            return (InputStream) obj;
        }

        if (obj instanceof String) {
            return IOUtils.toInputStream((String) obj, StandardCharsets.UTF_8);
        }

        if (obj instanceof Form) {
            return IOUtils.toInputStream(UrlUtils.urlEncodeMultivaluedParams(((Form) obj).asMap()), StandardCharsets.UTF_8);
        }

        if (obj instanceof Multipart) {
            return MultipartUtils.serializeMultipartForm((Multipart) obj);
        }

        if (entity.getMediaType() == APPLICATION_JSON_TYPE && OptionalClasses.JSON != null) {
            final MinijaxApplicationContext application = MinijaxApplicationContext.getApplicationContext();
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            application.writeEntity(obj, entity.getMediaType(), outputStream);
            return IOUtils.toInputStream(outputStream.toString(), StandardCharsets.UTF_8);
        }

        throw new UnsupportedOperationException("Unknown entity type: " + obj.getClass());
    }
}
