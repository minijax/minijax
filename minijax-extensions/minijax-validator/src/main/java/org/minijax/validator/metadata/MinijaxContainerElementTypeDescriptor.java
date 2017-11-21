package org.minijax.validator.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.util.HashSet;
import java.util.Set;

import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ContainerElementTypeDescriptor;
import javax.validation.metadata.GroupConversionDescriptor;

public class MinijaxContainerElementTypeDescriptor extends MinijaxElementDescriptor implements ContainerElementTypeDescriptor {
    private final Class<?> containerClass;
    private final int argumentIndex;

    public MinijaxContainerElementTypeDescriptor(
            final Class<?> elementClass,
            final Class<?> containerClass,
            final int argumentIndex,
            final Set<ConstraintDescriptor<?>> constraintDescriptors) {

        super(elementClass, constraintDescriptors);
        this.containerClass = containerClass;
        this.argumentIndex = argumentIndex;
    }

    @Override
    public Class<?> getContainerClass() {
        return containerClass;
    }

    @Override
    public Integer getTypeArgumentIndex() {
        return argumentIndex;
    }

    /*
     * Not supported
     */

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

    public static Set<ContainerElementTypeDescriptor> build(
            final Class<?> elementClass,
            final AnnotatedParameterizedType annotatedType) {

        final Set<ContainerElementTypeDescriptor> result = new HashSet<>();
        final Class<?> containerClass = ReflectionUtils.getRawType(annotatedType);
//        final Type containerType = annotatedType.getType();
//        final Class<?> containerClass;
//        if (containerType instanceof Class) {
//            containerClass = (Class<?>) containerType;
//        } else if (containerType instanceof ParameterizedType) {
//            containerClass = (Class<?>) ((ParameterizedType) containerType).getRawType();
//        } else {
//            throw new ValidationException("unknown type: " + containerType.getClass());
//        }

        int argIndex = 0;

        for (final AnnotatedType typeArg : annotatedType.getAnnotatedActualTypeArguments()) {
            final Set<ConstraintDescriptor<?>> constraintDescriptors = new HashSet<>();

            for (final Annotation annotation : typeArg.getAnnotations()) {
                final MinijaxConstraintDescriptor<?> constraintDescriptor = MinijaxConstraintDescriptor.build(typeArg, annotation);
                if (constraintDescriptor != null) {
                    constraintDescriptors.add(constraintDescriptor);
                }
            }

            if (!constraintDescriptors.isEmpty()) {
                result.add(new MinijaxContainerElementTypeDescriptor(elementClass, containerClass, argIndex, constraintDescriptors));
            }

            argIndex++;
        }

        return result;
    }
}
