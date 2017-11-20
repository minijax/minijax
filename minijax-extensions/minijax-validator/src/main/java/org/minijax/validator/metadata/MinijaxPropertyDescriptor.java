package org.minijax.validator.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Constraint;
import javax.validation.ValidationException;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ContainerElementTypeDescriptor;
import javax.validation.metadata.GroupConversionDescriptor;
import javax.validation.metadata.PropertyDescriptor;

public class MinijaxPropertyDescriptor extends MinijaxElementDescriptor implements PropertyDescriptor {
    private final Field field;

    public MinijaxPropertyDescriptor(final Field field) {
        super(field.getDeclaringClass(), buildConstraintDescriptors(field));
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    @Override
    public String getPropertyName() {
        return field.getName();
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

    private static Set<ConstraintDescriptor<?>> buildConstraintDescriptors(final Field field) {
        final Set<ConstraintDescriptor<?>> result = new HashSet<>();
        final AnnotatedType annotatedType = field.getAnnotatedType();

        for (final Annotation annotation : field.getAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(Constraint.class)) {
                try {
                    result.add(MinijaxConstraintDescriptor.build(annotatedType, annotation));
                } catch (final ReflectiveOperationException ex) {
                    throw new ValidationException(ex);
                }
            }
        }

        // 2.1.3. Container element constraints
        // It is possible to specify constraints directly on the type argument of a
        // parameterized type: these constraints are called container element
        // constraints.
        //
        // This requires that ElementType.TYPE_USE is specified via @Target in the
        // constraint definition. As of Bean Validation 2.0, built-in Bean Validation as
        // well as Hibernate Validator specific constraints specify ElementType.TYPE_USE
        // and can be used directly in this context.
        //
        // Hibernate Validator validates container element constraints specified on the
        // following standard Java containers:
        //
        // implementations of java.util.Iterable (e.g. Lists, Sets),
        //
        // implementations of java.util.Map, with support for keys and values,
        //
        // java.util.Optional, java.util.OptionalInt, java.util.OptionalDouble,
        // java.util.OptionalLong,

        final Class<?> cls = field.getClass();

        if (Iterable.class.isAssignableFrom(cls)) {

        }

        return result;
    }
}
