package org.minijax.rs.cdi;

import java.lang.annotation.Annotation;

import org.minijax.cdi.MinijaxProvider;
import org.minijax.cdi.annotation.TypeAnnotationProcessor;

public class RequestScopedAnnotationProcessor<T> implements TypeAnnotationProcessor<T> {

    @Override
    public MinijaxProvider<T> buildProvider(final MinijaxProvider<T> sourceProvider, final Annotation[] annotations) {
        return new RequestScopedProvider<>(sourceProvider);
    }
}
