package org.minijax.cdi.annotation;

import java.lang.annotation.Annotation;

import org.minijax.cdi.MinijaxProvider;

public interface TypeAnnotationProcessor<T> {

    MinijaxProvider<T> buildProvider(MinijaxProvider<T> sourceProvider, Annotation[] annotations);
}
