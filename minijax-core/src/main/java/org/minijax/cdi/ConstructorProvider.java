package org.minijax.cdi;

import java.lang.reflect.Constructor;

import javax.inject.Provider;

public class ConstructorProvider<T> implements Provider<T> {
    private final Constructor<T> ctor;
    private final Provider<?>[] paramProviders;
    private final FieldProvider<?>[] fieldProviders;

    public ConstructorProvider(
            final Constructor<T> ctor,
            final Provider<?>[] paramProviders,
            final FieldProvider<?>[] fieldProviders) {
        this.ctor = ctor;
        this.paramProviders = paramProviders;
        this.fieldProviders = fieldProviders;
    }

    @Override
    public T get() {
        try {
            final T result = ctor.newInstance(getParams());

            for (final FieldProvider<?> fieldProvider : fieldProviders) {
                final Object value;
                if (fieldProvider.isInjectProvider()) {
                    value = fieldProvider.getProvider();
                } else {
                    value = fieldProvider.getProvider().get();
                }
                fieldProvider.getField().set(result, value);
            }

            return result;

        } catch (final Exception e) {
            throw new InjectException(String.format("Can't instantiate %s", ctor), e);
        }
    }

    private Object[] getParams() {
        final Object[] params = new Object[paramProviders.length];
        for (int i = 0; i < paramProviders.length; ++i) {
            params[i] = paramProviders[i].get();
        }
        return params;
    }
}
