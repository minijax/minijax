package org.minijax;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Providers;

import org.minijax.util.MediaTypeClassMap;
import org.minijax.util.MediaTypeUtils;
import org.minijax.util.UuidParamConverterProvider;

public class MinijaxProviders implements Providers {
    private final MinijaxApplication application;
    private final MediaTypeClassMap<MessageBodyReader<?>> readers;
    private final MediaTypeClassMap<MessageBodyWriter<?>> writers;
    private final MediaTypeClassMap<ExceptionMapper<?>> exceptionMappers;
    private final List<ParamConverterProvider> paramConverterProviders;


    public MinijaxProviders(final MinijaxApplication application) {
        this.application = application;
        readers = new MediaTypeClassMap<>();
        writers = new MediaTypeClassMap<>();
        exceptionMappers = new MediaTypeClassMap<>();
        paramConverterProviders = new ArrayList<>(getDefaultParamConverterProviders());
    }


    @SuppressWarnings("unchecked")
    public void register(final Class<?> c) {
        if (MessageBodyReader.class.isAssignableFrom(c)) {
            readers.add((Class<MessageBodyReader<?>>) c, MediaTypeUtils.parseMediaTypes(c.getAnnotation(Consumes.class)));
        }

        if (MessageBodyWriter.class.isAssignableFrom(c)) {
            writers.add((Class<MessageBodyWriter<?>>) c, MediaTypeUtils.parseMediaTypes(c.getAnnotation(Produces.class)));
        }

        if (ExceptionMapper.class.isAssignableFrom(c)) {
            exceptionMappers.add((Class<ExceptionMapper<?>>) c, MediaTypeUtils.parseMediaTypes(c.getAnnotation(Produces.class)));
        }

        if (ParamConverterProvider.class.isAssignableFrom(c)) {
            paramConverterProviders.add((ParamConverterProvider) application.get(c));
        }
    }


    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <T> MessageBodyReader<T> getMessageBodyReader(
            final Class<T> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType) {

        for (final Class<? extends MessageBodyReader<?>> readerClass : readers.get(mediaType)) {
            final MessageBodyReader reader = application.get(readerClass);
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

        for (final Class<? extends MessageBodyWriter<?>> writerClass : writers.get(mediaType)) {
            final MessageBodyWriter writer = application.get(writerClass);
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
        for (final Class<? extends ExceptionMapper<?>> exceptionMapperClass : exceptionMappers.get(mediaType)) {
            final ParameterizedType parameterizedType = (ParameterizedType) exceptionMapperClass.getGenericInterfaces()[0];
            final Class<? extends Exception> exClass = (Class<? extends Exception>) parameterizedType.getActualTypeArguments()[0];
            if (exClass.isAssignableFrom(type)) {
                return (ExceptionMapper<T>) application.get(exceptionMapperClass);
            }
        }
        return null;
    }


    public <T> ParamConverter<T> getParamConverter(final Class<T> rawType, final Type genericType, final Annotation[] annotations) {
        for (final ParamConverterProvider provider : paramConverterProviders) {
            final ParamConverter<T> converter = provider.getConverter(rawType, genericType, annotations);
            if (converter != null) {
                return converter;
            }
        }
        return null;
    }


    @Override
    public <T> ContextResolver<T> getContextResolver(final Class<T> contextType, final MediaType mediaType) {
        return new MinijaxContextResolver<>();
    }


    private static List<ParamConverterProvider> getDefaultParamConverterProviders() {
        return Collections.singletonList(new UuidParamConverterProvider());
    }
}
