package org.minijax.rs.cdi;

import java.lang.annotation.Annotation;

import org.minijax.cdi.annotation.FieldAnnotationProcessor;

abstract class AbstractParamAnnotationProcessor<T> implements FieldAnnotationProcessor<T> {

    @SuppressWarnings("unchecked")
    protected static <T extends Annotation> T getAnnotationByType(final Annotation[] annotations, final Class<T> c) {
        for (final Annotation annotation : annotations) {
            if (annotation.annotationType() == c) {
                return (T) annotation;
            }
        }
        return null;
    }
}
