package org.minijax.rs.converters;

import jakarta.ws.rs.ext.ParamConverter;

public class PrimitiveParamConverter<T> implements ParamConverter<T> {
    private final Class<T> c;

    public PrimitiveParamConverter(final Class<T> c) {
        this.c = c;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T fromString(final String str) {
        if (c == boolean.class) {
            return (T) Boolean.valueOf(str);
        }
        if (c == byte.class) {
            return (T) Byte.valueOf(str);
        }
        if (c == char.class) {
            return (T) (str.isEmpty() ? null : ((Character) str.charAt(0)));
        }
        if (c == double.class) {
            return (T) Double.valueOf(str);
        }
        if (c == float.class) {
            return (T) Float.valueOf(str);
        }
        if (c == int.class) {
            return (T) Integer.valueOf(str);
        }
        if (c == long.class) {
            return (T) Long.valueOf(str);
        }
        if (c == short.class) {
            return (T) Short.valueOf(str);
        }
        throw new IllegalArgumentException("Unrecognized primitive (" + c + ")");
    }

    @Override
    public String toString(final T value) {
        return value.toString();
    }
}
