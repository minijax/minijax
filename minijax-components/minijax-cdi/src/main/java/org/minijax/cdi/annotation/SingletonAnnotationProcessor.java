package org.minijax.cdi.annotation;

import java.lang.annotation.Annotation;

import org.minijax.cdi.MinijaxProvider;
import org.minijax.cdi.SingletonProvider;

public class SingletonAnnotationProcessor<T> implements TypeAnnotationProcessor<T> {

    @Override
    public MinijaxProvider<T> buildProvider(final MinijaxProvider<T> sourceProvider, final Annotation[] annotations) {
        return new SingletonProvider<>(sourceProvider);
    }
}
