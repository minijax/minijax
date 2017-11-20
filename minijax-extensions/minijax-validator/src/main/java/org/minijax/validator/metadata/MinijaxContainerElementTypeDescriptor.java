package org.minijax.validator.metadata;

import java.util.Set;

import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ContainerElementTypeDescriptor;
import javax.validation.metadata.GroupConversionDescriptor;

public class MinijaxContainerElementTypeDescriptor extends MinijaxElementDescriptor implements ContainerElementTypeDescriptor {

    public MinijaxContainerElementTypeDescriptor(
            final Class<?> elementClass,
            final Set<ConstraintDescriptor<?>> constraintDescriptors) {

        super(elementClass, constraintDescriptors);
    }

    @Override
    public boolean isCascaded() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<GroupConversionDescriptor> getGroupConversions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<ContainerElementTypeDescriptor> getConstrainedContainerElementTypes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer getTypeArgumentIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<?> getContainerClass() {
        throw new UnsupportedOperationException();
    }
}
