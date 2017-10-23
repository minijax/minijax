package org.minijax.cdi;

import java.lang.reflect.Field;

import javax.inject.Provider;

public class FieldProvider<T> {
    private final Field field;
    private final Provider<T> provider;
    private final boolean injectProvider;

    public FieldProvider(final Field field, final Provider<T> provider) {
        this.field = field;
        this.provider = provider;
        injectProvider = field.getType() == Provider.class;
    }

    public Field getField() {
        return field;
    }

    public Provider<T> getProvider() {
        return provider;
    }

    public boolean isInjectProvider() {
        return injectProvider;
    }
}
