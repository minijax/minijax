package org.minijax.json;

import static jakarta.ws.rs.core.MediaType.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;

@Singleton
@Produces(APPLICATION_JSON)
public class MinijaxJsonWriter implements MessageBodyWriter<Object> {

    @Override
    public boolean isWriteable(
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType) {

        return mediaType != null && mediaType.isCompatible(APPLICATION_JSON_TYPE);
    }

    @Override
    public void writeTo(
            final Object t,
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType,
            final MultivaluedMap<String, Object> httpHeaders,
            final OutputStream entityStream)
                    throws IOException {

        Json.getObjectMapper().writeValue(entityStream, t);
    }
}
