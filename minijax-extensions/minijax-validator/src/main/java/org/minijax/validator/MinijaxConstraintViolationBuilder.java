package org.minijax.validator;

import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

public class MinijaxConstraintViolationBuilder<T> implements ConstraintViolationBuilder {
    private final MinijaxConstraintValidatorContext<T> context;

    public MinijaxConstraintViolationBuilder(final MinijaxConstraintValidatorContext<T> context) {
        this.context = context;
    }

    @Override
    public NodeBuilderDefinedContext addNode(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NodeBuilderCustomizableContext addPropertyNode(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LeafNodeBuilderCustomizableContext addBeanNode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ContainerElementNodeBuilderCustomizableContext addContainerElementNode(
            final String name,
            final Class<?> containerType,
            final Integer typeArgumentIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NodeBuilderDefinedContext addParameterNode(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxConstraintValidatorContext<T> addConstraintViolation() {
        return context;
    }
}
