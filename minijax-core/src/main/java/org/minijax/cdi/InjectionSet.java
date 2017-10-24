package org.minijax.cdi;

public class InjectionSet {
    private final Class<?> type;
    private final FieldProvider<?>[] staticFieldProviders;
    private final FieldProvider<?>[] fieldProviders;
    private final MethodProvider[] staticMethodProviders;
    private final MethodProvider[] methodProviders;

    public InjectionSet(
            final Class<?> type,
            final FieldProvider<?>[] staticFieldProviders,
            final FieldProvider<?>[] fieldProviders,
            final MethodProvider[] staticMethodProviders,
            final MethodProvider[] methodProviders) {

        this.type = type;
        this.staticFieldProviders = staticFieldProviders;
        this.fieldProviders = fieldProviders;
        this.staticMethodProviders = staticMethodProviders;
        this.methodProviders = methodProviders;
    }

    public Class<?> getType() {
        return type;
    }

    public FieldProvider<?>[] getStaticFieldProviders() {
        return staticFieldProviders;
    }

    public FieldProvider<?>[] getFieldProviders() {
        return fieldProviders;
    }

    public MethodProvider[] getStaticMethodProviders() {
        return staticMethodProviders;
    }

    public MethodProvider[] getMethodProviders() {
        return methodProviders;
    }
}
