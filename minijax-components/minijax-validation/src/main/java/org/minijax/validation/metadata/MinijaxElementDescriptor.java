package org.minijax.validation.metadata;

import java.util.Set;

import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.validation.metadata.ElementDescriptor;

public abstract class MinijaxElementDescriptor implements ElementDescriptor {
    private final Class<?> elementClass;
    private final Set<ConstraintDescriptor<?>> constraintDescriptors;

    protected MinijaxElementDescriptor(
            final Class<?> elementClass,
            final Set<ConstraintDescriptor<?>> constraintDescriptors) {
        this.elementClass = elementClass;
        this.constraintDescriptors = constraintDescriptors;
    }

    @Override
    public Class<?> getElementClass() {
        return elementClass;
    }

    @Override
    public boolean hasConstraints() {
        return !constraintDescriptors.isEmpty();
    }

    @Override
    public Set<ConstraintDescriptor<?>> getConstraintDescriptors() {
        return constraintDescriptors;
    }

    @Override
    public ConstraintFinder findConstraints() {
        throw new UnsupportedOperationException();
    }
}
