package org.minijax.validator.metadata;

import java.util.Set;

import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ElementDescriptor;

public class MinijaxElementDescriptor implements ElementDescriptor {
    private final Class<?> elementClass;
    private final Set<ConstraintDescriptor<?>> constraintDescriptors;

    public MinijaxElementDescriptor(
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
