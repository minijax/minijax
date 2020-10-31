package org.minijax.rs.converters;

import java.lang.reflect.Method;

import jakarta.ws.rs.ext.ParamConverter;

import org.minijax.commons.MinijaxException;

public class ValueOfParamConverter<T> implements ParamConverter<T> {
    private final Method valueOf;

    public ValueOfParamConverter(final Method valueOf) {
        this.valueOf = valueOf;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T fromString(final String str) {
        if (str == null) {
            return null;
        }
        try {
            return (T) valueOf.invoke(null, str);
        } catch (ReflectiveOperationException | IllegalArgumentException ex) {
            throw new MinijaxException("Error creating param with string constructor", ex);
        }
    }

    @Override
    public String toString(final T value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }
}
