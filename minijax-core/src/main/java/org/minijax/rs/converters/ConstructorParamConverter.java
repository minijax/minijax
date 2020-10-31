package org.minijax.rs.converters;

import java.lang.reflect.Constructor;

import jakarta.ws.rs.ext.ParamConverter;

import org.minijax.commons.MinijaxException;

public class ConstructorParamConverter<T> implements ParamConverter<T> {
    private final Constructor<T> ctor;

    public ConstructorParamConverter(final Constructor<T> ctor) {
        this.ctor = ctor;
    }

    @Override
    public T fromString(final String str) {
        if (str == null) {
            return null;
        }
        try {
            return ctor.newInstance(str);
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
