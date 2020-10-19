package org.minijax.mustache;

import static jakarta.ws.rs.core.MediaType.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;

import org.minijax.view.View;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

@Singleton
@Produces(TEXT_HTML)
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
                    throws IOException {

        final Mustache mustache = mf.compile("templates/" + page.getTemplateName() + ".mustache");

        try (final OutputStreamWriter writer = new OutputStreamWriter(entityStream)) {
            mustache.execute(writer, page.getModel()).flush();
        }
    }
}
