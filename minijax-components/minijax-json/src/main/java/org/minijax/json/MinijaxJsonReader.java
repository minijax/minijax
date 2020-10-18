package org.minijax.json;

import static jakarta.ws.rs.core.MediaType.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbException;

import jakarta.inject.Singleton;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;

@Singleton
@Consumes(APPLICATION_JSON)
public class MinijaxJsonReader implements MessageBodyReader<Object> {

    @Override
    public boolean isReadable(
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType) {

        return mediaType != null && mediaType.isCompatible(APPLICATION_JSON_TYPE);
    }

    @Override
    public Object readFrom(
            final Class<Object> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType,
            final MultivaluedMap<String, String> httpHeaders,
            final InputStream entityStream)
                    throws IOException {

        final Jsonb objectMapper = Json.getObjectMapper();

        try {
            if (genericType != null) {
                return objectMapper.fromJson(entityStream, genericType);
            } else {
                return objectMapper.fromJson(entityStream, type);
            }
        } catch (final JsonbException ex) {
            throw new BadRequestException(ex.getMessage(), ex);
        }
    }
}
