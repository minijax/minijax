package org.minijax.validation.metadata;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import jakarta.validation.ValidationException;

public class ReflectionUtils {

    ReflectionUtils() {
        throw new UnsupportedOperationException();
    }

    public static Class<?> getRawType(final AnnotatedType annotatedType) {
        final Type containerType = annotatedType.getType();
        if (containerType instanceof Class) {
            return (Class<?>) containerType;
        } else if (containerType instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) containerType).getRawType();
        } else {
            throw new ValidationException("Unknown type: " + containerType.getClass());
        }
    }
}
