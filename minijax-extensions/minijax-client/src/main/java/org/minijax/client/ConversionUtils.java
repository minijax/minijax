package org.minijax.client;

import java.io.IOException;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.minijax.MinijaxApplication;
import org.minijax.MinijaxException;

public class ConversionUtils {

    ConversionUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T> T convertApacheToJax(final HttpEntity apacheEntity, final Class<T> targetClass) {
        final MediaType mediaType = MediaType.valueOf(apacheEntity.getContentType().getValue());

        try {
            return MinijaxApplication.getApplication().readEntity(
                    targetClass,
                    null,
                    null,
                    mediaType,
                    null,
                    apacheEntity.getContent());
        } catch (final IOException ex) {
            throw new MinijaxException("Error converting input stream: " + ex.getMessage(), ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T convertToGenericType(final HttpEntity apacheEntity, final GenericType<T> genericType) {
        final MediaType mediaType = MediaType.valueOf(apacheEntity.getContentType().getValue());

        try {
            return MinijaxApplication.getApplication().readEntity(
                    (Class<T>) genericType.getRawType(),
                    genericType.getType(),
                    null,
                    mediaType,
                    null,
                    apacheEntity.getContent());
        } catch (final IOException ex) {
            throw new MinijaxException("Error converting input stream: " + ex.getMessage(), ex);
        }
    }
}
