package org.minijax.validation.metadata;

import static java.util.Collections.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.ConstructorDescriptor;
import jakarta.validation.metadata.MethodDescriptor;
import jakarta.validation.metadata.MethodType;
import jakarta.validation.metadata.PropertyDescriptor;

public class MinijaxBeanDescriptor extends MinijaxElementDescriptor implements BeanDescriptor {
    private final Set<PropertyDescriptor> constrainedProperties;

    public MinijaxBeanDescriptor(final Class<?> elementClass) {
        super(elementClass, emptySet());
        constrainedProperties = buildProperties(elementClass);
    }

    @Override
    public boolean isBeanConstrained() {
        return !constrainedProperties.isEmpty();
    }

    @Override
    public Set<PropertyDescriptor> getConstrainedProperties() {
        return constrainedProperties;
    }

    @Override
    public PropertyDescriptor getConstraintsForProperty(final String propertyName) {
        for (final PropertyDescriptor propertyDescriptor : constrainedProperties) {
            if (propertyDescriptor.getPropertyName().equals(propertyName)) {
                return propertyDescriptor;
            }
        }
        return null;
    }

    /*
     * Not implemented
     */

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

    private static Set<PropertyDescriptor> buildProperties(final Class<?> c) {
        final Set<PropertyDescriptor> results = new HashSet<>();
        Class<?> currClass = c;

        while (currClass != null) {
            buildFields(results, currClass);
            buildGetters(results, currClass);
            currClass = currClass.getSuperclass();
        }

        return results;
    }

    private static void buildFields(final Set<PropertyDescriptor> results, final Class<?> currClass) {
        for (final Field field : currClass.getDeclaredFields()) {
            final MinijaxFieldDescriptor fieldDescriptor = new MinijaxFieldDescriptor(field);
            if (fieldDescriptor.hasConstraints()) {
                field.setAccessible(true);
                results.add(fieldDescriptor);
            }
        }
    }

    private static void buildGetters(final Set<PropertyDescriptor> results, final Class<?> currClass) {
        for (final Method method : currClass.getDeclaredMethods()) {
            if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                final MinijaxGetterDescriptor getterDescriptor = new MinijaxGetterDescriptor(method);
                if (getterDescriptor.hasConstraints()) {
                    method.setAccessible(true);
                    results.add(getterDescriptor);
                }
            }
        }
    }
}
