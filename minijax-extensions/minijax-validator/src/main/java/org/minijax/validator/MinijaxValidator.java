package org.minijax.validator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.minijax.validator.MinijaxPath.MinijaxPropertyNode;
import org.minijax.validator.metadata.MinijaxBeanDescriptor;
import org.minijax.validator.metadata.MinijaxConstraintDescriptor;
import org.minijax.validator.metadata.MinijaxPropertyDescriptor;

public class MinijaxValidator implements Validator, ExecutableValidator {
    private final Map<Class<?>, BeanDescriptor> beanDescriptors;

    public MinijaxValidator() {
        beanDescriptors = new HashMap<>();
    }

    @Override
    public BeanDescriptor getConstraintsForClass(final Class<?> clazz) {
        return beanDescriptors.computeIfAbsent(clazz, MinijaxBeanDescriptor::new);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> Set<ConstraintViolation<T>> validate(final T object, final Class<?>... groups) {
        final Set<ConstraintViolation<T>> result = new HashSet<>();
        final BeanDescriptor descriptor = getConstraintsForClass(object.getClass());
        final ConstraintValidatorContext context = null;

        for (final PropertyDescriptor propertyDescriptor : descriptor.getConstrainedProperties()) {
            final Field field = ((MinijaxPropertyDescriptor) propertyDescriptor).getField();
            final Object value;
            try {
                value = field.get(object);
            } catch (final ReflectiveOperationException ex) {
                throw new ValidationException(ex);
            }

            for (final ConstraintDescriptor constraintDescriptor : propertyDescriptor.getConstraintDescriptors()) {
                final ConstraintValidator constraintValidator = ((MinijaxConstraintDescriptor) constraintDescriptor).getConstraintValidator();
                if (!constraintValidator.isValid(value, context)) {
                    final Path path = new MinijaxPath(Arrays.asList(new MinijaxPropertyNode(0, field.getName())));
                    result.add(new MinijaxConstraintViolation<>(object, path, constraintDescriptor));
                }
            }
        }

        return result;
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateProperty(final T object, final String propertyName, final Class<?>... groups) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateValue(final Class<T> beanType, final String propertyName, final Object value, final Class<?>... groups) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateParameters(final T object, final Method method, final Object[] parameterValues, final Class<?>... groups) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateReturnValue(final T object, final Method method, final Object returnValue, final Class<?>... groups) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateConstructorParameters(
            final Constructor<? extends T> constructor,
            final Object[] parameterValues,
            final Class<?>... groups) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateConstructorReturnValue(
            final Constructor<? extends T> constructor,
            final T createdObject,
            final Class<?>... groups) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(final Class<T> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExecutableValidator forExecutables() {
        return this;
    }
}
