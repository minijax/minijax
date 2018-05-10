package org.minijax.json;

import static javax.ws.rs.core.MediaType.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Singleton;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

        final ObjectMapper objectMapper = Json.getObjectMapper();

        try {
            if (genericType != null) {
                return objectMapper.readValue(entityStream, objectMapper.getTypeFactory().constructType(genericType));
            } else {
                return objectMapper.readValue(entityStream, type);
            }
        } catch (final JsonProcessingException ex) {
            throw new BadRequestException(ex.getMessage(), ex);
        }
    }
}
