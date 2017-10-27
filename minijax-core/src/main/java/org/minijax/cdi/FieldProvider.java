package org.minijax.cdi;

import java.lang.reflect.Field;

import javax.inject.Provider;

class FieldProvider<T> {
    private final Field field;
    private final Provider<T> provider;

    public FieldProvider(final Field field, final Provider<T> provider) {
        this.field = field;
        this.provider = provider;
    }

    public Field getField() {
        return field;
    }

    public Provider<T> getProvider() {
        return provider;
    }
}
