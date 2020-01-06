package org.minijax.rs;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

public class WidgetWriter implements MessageBodyWriter<Widget> {

    @Override
    public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return type == Widget.class;
    }

    @Override
    public void writeTo(
            final Widget widget,
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType,
            final MultivaluedMap<String, Object> httpHeaders,
            final OutputStream entityStream)
                    throws IOException, WebApplicationException {

        entityStream.write(String.format("(widget %s %s)", widget.getId(), widget.getValue()).getBytes(StandardCharsets.UTF_8));
    }
}
