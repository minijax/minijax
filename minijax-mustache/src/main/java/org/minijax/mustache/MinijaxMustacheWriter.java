package org.minijax.mustache;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class MinijaxMustacheWriter implements MessageBodyWriter<View> {

    @Inject
    private MustacheFactory mf;

    @Override
    public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return type != null && View.class.isAssignableFrom(type);
    }

    @Override
    public void writeTo(
            final View page,
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType,
            final MultivaluedMap<String, Object> httpHeaders,
            final OutputStream entityStream)
                    throws IOException, WebApplicationException {

        final Mustache mustache = mf.compile("templates/" + page.getTemplateName() + ".mustache");

        try (final OutputStreamWriter writer = new OutputStreamWriter(entityStream)) {
            mustache.execute(writer, page).flush();
        }
    }
}
