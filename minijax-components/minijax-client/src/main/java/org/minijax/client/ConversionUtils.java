package org.minijax.client;

import java.io.IOException;
import java.io.InputStream;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;

import org.minijax.commons.MinijaxException;
import org.minijax.rs.util.EntityUtils;

public class ConversionUtils {

    ConversionUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T> T convertToType(final MediaType mediaType, final InputStream inputStream, final Class<T> targetClass) {
        if (inputStream == null) {
            return null;
        }

        try {
            return EntityUtils.readEntity(
                    targetClass,
                    null,
                    null,
                    mediaType,
                    null,
                    inputStream);
        } catch (final IOException ex) {
            throw new MinijaxException("Error converting input stream: " + ex.getMessage(), ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T convertToGenericType(final MediaType mediaType, final InputStream inputStream, final GenericType<T> genericType) {
        if (inputStream == null) {
            return null;
        }

        try {
            return EntityUtils.readEntity(
                    (Class<T>) genericType.getRawType(),
                    genericType.getType(),
                    null,
                    mediaType,
                    null,
                    inputStream);
        } catch (final IOException ex) {
            throw new MinijaxException("Error converting input stream: " + ex.getMessage(), ex);
        }
    }
}
