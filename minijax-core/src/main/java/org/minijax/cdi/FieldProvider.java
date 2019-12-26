package org.minijax.cdi;

import java.lang.reflect.Field;

class FieldProvider<T> {
    private final Field field;
    private final MinijaxProvider<T> provider;

    public FieldProvider(final Field field, final MinijaxProvider<T> provider) {
        this.field = field;
        this.provider = provider;
    }

    public Field getField() {
        return field;
    }

    public MinijaxProvider<T> getProvider() {
        return provider;
    }
}
