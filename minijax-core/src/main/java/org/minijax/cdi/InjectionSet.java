package org.minijax.cdi;

import java.util.List;

public class InjectionSet<T> {
    private final Class<T> type;
    private final List<FieldProvider<?>> staticFieldProviders;
    private final List<FieldProvider<?>> fieldProviders;
    private final List<MethodProvider> staticMethodProviders;
    private final List<MethodProvider> methodProviders;

    public InjectionSet(
            final Class<T> type,
            final List<FieldProvider<?>> staticFieldProviders,
            final List<FieldProvider<?>> fieldProviders,
            final List<MethodProvider> staticMethodProviders,
            final List<MethodProvider> methodProviders) {

        this.type = type;
        this.staticFieldProviders = staticFieldProviders;
        this.fieldProviders = fieldProviders;
        this.staticMethodProviders = staticMethodProviders;
        this.methodProviders = methodProviders;
    }

    public Class<T> getType() {
        return type;
    }

    public List<FieldProvider<?>> getStaticFieldProviders() {
        return staticFieldProviders;
    }

    @SuppressWarnings("squid:S1452")
    public List<FieldProvider<?>> getFieldProviders() {
        return fieldProviders;
    }

    @SuppressWarnings("squid:S1452")
    public List<MethodProvider> getStaticMethodProviders() {
        return staticMethodProviders;
    }

    public List<MethodProvider> getMethodProviders() {
        return methodProviders;
    }
}
