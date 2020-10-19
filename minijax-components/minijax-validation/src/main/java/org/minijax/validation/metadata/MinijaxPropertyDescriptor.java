package org.minijax.validation.metadata;

import static java.util.Collections.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.validation.metadata.ContainerElementTypeDescriptor;
import jakarta.validation.metadata.GroupConversionDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;

public abstract class MinijaxPropertyDescriptor extends MinijaxElementDescriptor implements PropertyDescriptor {
    private final Set<ContainerElementTypeDescriptor> constrainedContainerElementTypes;

    protected MinijaxPropertyDescriptor(final Class<?> elementClass, final AnnotatedType annotatedType, final Annotation[] annotations) {
        super(elementClass, buildConstraintDescriptors(annotatedType, annotations));

        if (annotatedType instanceof AnnotatedParameterizedType) {
            constrainedContainerElementTypes = MinijaxContainerElementTypeDescriptor.build(elementClass, (AnnotatedParameterizedType) annotatedType);
        } else {
            constrainedContainerElementTypes = emptySet();
        }
    }

    @Override
    public boolean hasConstraints() {
        return super.hasConstraints() || !constrainedContainerElementTypes.isEmpty();
    }

    @Override
    public Set<ContainerElementTypeDescriptor> getConstrainedContainerElementTypes() {
        return constrainedContainerElementTypes;
    }

    @Override
    public boolean isCascaded() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<GroupConversionDescriptor> getGroupConversions() {
        throw new UnsupportedOperationException();
    }

    public abstract Object getValue(final Object object);

    private static Set<ConstraintDescriptor<?>> buildConstraintDescriptors(final AnnotatedType annotatedType, final Annotation[] annotations) {
        final Set<ConstraintDescriptor<?>> result = new HashSet<>();

        for (final Annotation annotation : annotations) {
            final MinijaxConstraintDescriptor<?> constraintDescriptor = MinijaxConstraintDescriptor.build(annotatedType, annotation);
            if (constraintDescriptor != null) {
                result.add(constraintDescriptor);
            }
        }

        return result;
    }
}
