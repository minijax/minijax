package org.minijax.cdi.annotation;

import java.lang.annotation.Annotation;

import org.minijax.cdi.MinijaxInjectorState;
import org.minijax.cdi.MinijaxProvider;

public interface FieldAnnotationProcessor<T> {

    MinijaxProvider<T> buildProvider(MinijaxInjectorState state, Class<T> type, Annotation[] annotations);

}
