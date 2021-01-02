package org.minijax.rs;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Providers;

import org.minijax.rs.writers.FileBodyWriter;
import org.minijax.rs.writers.InputStreamBodyWriter;
import org.minijax.rs.writers.StringBodyWriter;

public class MinijaxProviders implements Providers {
    private static final StringBodyWriter STRING_WRITER = new StringBodyWriter();
    private static final InputStreamBodyWriter INPUT_STREAM_WRITER = new InputStreamBodyWriter();
    private static final FileBodyWriter FILE_WRITER = new FileBodyWriter();
    private final MinijaxRequestContext context;

    public MinijaxProviders(final MinijaxRequestContext context) {
        this.context = context;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <T> MessageBodyReader<T> getMessageBodyReader(
            final Class<T> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType) {

        for (final Class<? extends MessageBodyReader<?>> readerClass : context.getApplication().getReaders().get(mediaType)) {
            final MessageBodyReader reader = context.getResource(readerClass);
            if (reader.isReadable(type, genericType, annotations, mediaType)) {
                return reader;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <T> MessageBodyWriter<T> getMessageBodyWriter(
            final Class<T> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType) {

        if (STRING_WRITER.isWriteable(type, genericType, annotations, mediaType)) {
            return (MessageBodyWriter<T>) STRING_WRITER;
        }

        if (INPUT_STREAM_WRITER.isWriteable(type, genericType, annotations, mediaType)) {
            return (MessageBodyWriter<T>) INPUT_STREAM_WRITER;
        }

        if (FILE_WRITER.isWriteable(type, genericType, annotations, mediaType)) {
            return (MessageBodyWriter<T>) FILE_WRITER;
        }

        for (final Class<? extends MessageBodyWriter<?>> writerClass : context.getApplication().getWriters().get(mediaType)) {
            final MessageBodyWriter writer = context.getResource(writerClass);
            if (writer.isWriteable(type, genericType, annotations, mediaType)) {
                return writer;
            }
        }
        return null;
    }

    @Override
    public <T extends Throwable> ExceptionMapper<T> getExceptionMapper(final Class<T> type) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get an exception mapping provider for a particular class of exception.
     *
     * This is non-standard (i.e., not in the official JAX-RS spec), but there is evidence that
     * it will be in a future version:  https://github.com/jax-rs/api/issues/328 ("JAX_RS_SPEC-323").
     *
     * @param type
     * @param mediaType
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends Throwable> ExceptionMapper<T> getExceptionMapper(final Class<T> type, final MediaType mediaType) {
        for (final Class<? extends ExceptionMapper<?>> exceptionMapperClass : context.getApplication().getExceptionMappers().get(mediaType)) {
            final ParameterizedType parameterizedType = (ParameterizedType) exceptionMapperClass.getGenericInterfaces()[0];
            final Class<? extends Exception> exClass = (Class<? extends Exception>) parameterizedType.getActualTypeArguments()[0];
            if (exClass.isAssignableFrom(type)) {
                return (ExceptionMapper<T>) context.getResource(exceptionMapperClass);
            }
        }
        return null;
    }

    public <T> ParamConverter<T> getParamConverter(final Class<T> rawType, final Type genericType, final Annotation[] annotations) {
        for (final ParamConverterProvider provider : context.getApplication().getParamConverterProviders()) {
            final ParamConverter<T> converter = provider.getConverter(rawType, genericType, annotations);
            if (converter != null) {
                return converter;
            }
        }
        return null;
    }

    @Override
    public <T> ContextResolver<T> getContextResolver(final Class<T> contextType, final MediaType mediaType) {
        throw new UnsupportedOperationException();
    }
}
