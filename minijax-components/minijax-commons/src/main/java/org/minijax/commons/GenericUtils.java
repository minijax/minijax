package org.minijax.commons;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericUtils {

    GenericUtils() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGenericParameterType(final Type genericType, final int index) {
        if (!(genericType instanceof ParameterizedType)) {
            throw new MinijaxException("Generic type missing generic parameters");
        }

        return (Class<T>) ((ParameterizedType) genericType).getActualTypeArguments()[index];
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGenericParameterType(final Class<?> baseType, final Class<?> interfaceType, final int index) {
        for (final Type type : baseType.getGenericInterfaces()) {
            if (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType().equals(interfaceType)) {
                return (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[index];
            }
        }
        return null;
    }
}
