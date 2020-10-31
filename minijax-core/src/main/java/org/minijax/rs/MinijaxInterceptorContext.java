package org.minijax.rs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.InterceptorContext;

public abstract class MinijaxInterceptorContext implements InterceptorContext {

    @Override
    public Object getProperty(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> getPropertyNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setProperty(final String name, final Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeProperty(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Annotation[] getAnnotations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAnnotations(final Annotation[] annotations) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<?> getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setType(final Class<?> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Type getGenericType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setGenericType(final Type genericType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MediaType getMediaType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMediaType(final MediaType mediaType) {
        throw new UnsupportedOperationException();
    }
}
