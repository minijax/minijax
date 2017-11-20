package org.minijax.validator.metadata;

import static java.util.Collections.*;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ConstructorDescriptor;
import javax.validation.metadata.MethodDescriptor;
import javax.validation.metadata.MethodType;
import javax.validation.metadata.PropertyDescriptor;

public class MinijaxBeanDescriptor extends MinijaxElementDescriptor implements BeanDescriptor {
    private final Set<PropertyDescriptor> constrainedProperties;

    public MinijaxBeanDescriptor(final Class<?> elementClass) {
        super(elementClass, buildConstraintDescriptors(elementClass));
        constrainedProperties = buildConstrainedProperties(elementClass);
    }

    @Override
    public boolean isBeanConstrained() {
        //throw new UnsupportedOperationException();
        return !constrainedProperties.isEmpty();
    }

    @Override
    public Set<PropertyDescriptor> getConstrainedProperties() {
        return constrainedProperties;
    }

    /*
     * Not implemented
     */

    @Override
    public PropertyDescriptor getConstraintsForProperty(final String propertyName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MethodDescriptor getConstraintsForMethod(final String methodName, final Class<?>... parameterTypes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<MethodDescriptor> getConstrainedMethods(final MethodType methodType, final MethodType... methodTypes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConstructorDescriptor getConstraintsForConstructor(final Class<?>... parameterTypes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<ConstructorDescriptor> getConstrainedConstructors() {
        throw new UnsupportedOperationException();
    }

    private static Set<ConstraintDescriptor<?>> buildConstraintDescriptors(final Class<?> c) {
        return emptySet();
    }

    private static Set<PropertyDescriptor> buildConstrainedProperties(final Class<?> c) {
        final Set<PropertyDescriptor> results = new HashSet<>();

        Class<?> currClass = c;
        while (currClass != null) {
            for (final Field field : currClass.getDeclaredFields()) {
                final MinijaxPropertyDescriptor propertyDescriptor = new MinijaxPropertyDescriptor(field);
                if (propertyDescriptor.hasConstraints()) {
                    field.setAccessible(true);
                    results.add(propertyDescriptor);
                }
            }
            currClass = currClass.getSuperclass();
        }

        return results;
    }
}
