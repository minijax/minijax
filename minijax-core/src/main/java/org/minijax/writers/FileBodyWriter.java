package org.minijax.writers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import org.minijax.util.IOUtils;

public class FileBodyWriter implements MessageBodyWriter<File> {

    @Override
    public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return type == File.class;
    }

    @Override
    public void writeTo(
            final File file,
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType,
            final MultivaluedMap<String, Object> httpHeaders,
            final OutputStream entityStream)
                    throws IOException {

        try (final FileInputStream inputStream = new FileInputStream(file)) {
            IOUtils.copy(inputStream, entityStream);
        }
    }
}
