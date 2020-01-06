package org.minijax.json;

import static javax.ws.rs.core.MediaType.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

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
