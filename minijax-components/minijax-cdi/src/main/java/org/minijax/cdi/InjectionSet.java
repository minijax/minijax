package org.minijax.cdi;

import java.util.List;

class InjectionSet<T> {
    private final List<FieldProvider<?>> fieldProviders;
    private final List<MethodProvider> methodProviders;

    public InjectionSet(
            final List<FieldProvider<?>> fieldProviders,
            final List<MethodProvider> methodProviders) {

        this.fieldProviders = fieldProviders;
        this.methodProviders = methodProviders;
    }

    @SuppressWarnings("squid:S1452")
    public List<FieldProvider<?>> getFieldProviders() {
        return fieldProviders;
    }

    public List<MethodProvider> getMethodProviders() {
        return methodProviders;
    }
}
