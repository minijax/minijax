package org.minijax.validator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ContainerElementTypeDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.minijax.validator.metadata.MinijaxBeanDescriptor;
import org.minijax.validator.metadata.MinijaxConstraintDescriptor;
import org.minijax.validator.metadata.MinijaxPropertyDescriptor;

public class MinijaxValidator implements Validator, ExecutableValidator {
    private final Map<Class<?>, MinijaxBeanDescriptor> beanDescriptors;

    public MinijaxValidator() {
        beanDescriptors = new HashMap<>();
    }

    @Override
    public MinijaxBeanDescriptor getConstraintsForClass(final Class<?> clazz) {
        return beanDescriptors.computeIfAbsent(clazz, MinijaxBeanDescriptor::new);
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validate(final T object, final Class<?>... groups) {
        final MinijaxConstraintValidatorContext<T> context = new MinijaxConstraintValidatorContext<>(object);
        final BeanDescriptor descriptor = getConstraintsForClass(object.getClass());

        for (final PropertyDescriptor propertyDescriptor : descriptor.getConstrainedProperties()) {
            final Object value = ((MinijaxPropertyDescriptor) propertyDescriptor).getValue(object);
            validateProperty(context, propertyDescriptor, value);
        }

        return context.getResult();
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateProperty(final T object, final String propertyName, final Class<?>... groups) {
        final MinijaxConstraintValidatorContext<T> context = new MinijaxConstraintValidatorContext<>(object);
        final BeanDescriptor descriptor = getConstraintsForClass(object.getClass());
        final PropertyDescriptor propertyDescriptor = descriptor.getConstraintsForProperty(propertyName);
        final Object value = ((MinijaxPropertyDescriptor) propertyDescriptor).getValue(object);
        validateProperty(context, propertyDescriptor, value);
        return context.getResult();
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateValue(final Class<T> beanType, final String propertyName, final Object value, final Class<?>... groups) {
        final MinijaxConstraintValidatorContext<T> context = new MinijaxConstraintValidatorContext<>(beanType);
        final PropertyDescriptor property = getConstraintsForClass(beanType).getConstraintsForProperty(propertyName);
        validateProperty(context, property, value);
        return context.getResult();
    }

    private <T> void validateProperty(
            final MinijaxConstraintValidatorContext<T> context,
            final PropertyDescriptor property,
            final Object value) {

        context.push(property);
        validatePropertyConstraints(context, property, value);
        validatePropertyElementConstraints(context, property, value);
        context.pop();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <T> void validatePropertyConstraints(
            final MinijaxConstraintValidatorContext<T> context,
            final PropertyDescriptor property,
            final Object value) {

        for (final ConstraintDescriptor constraint : property.getConstraintDescriptors()) {
            final ConstraintValidator validator = ((MinijaxConstraintDescriptor) constraint).getValidator();
            if (!validator.isValid(value, context)) {
                context.buildViolation(constraint, value);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private <T> void validatePropertyElementConstraints(
            final MinijaxConstraintValidatorContext<T> context,
            final PropertyDescriptor property,
            final Object value) {

        for (final ContainerElementTypeDescriptor descriptor : property.getConstrainedContainerElementTypes()) {
            for (final ConstraintDescriptor constraint : descriptor.getConstraintDescriptors()) {
                final ConstraintValidator validator = ((MinijaxConstraintDescriptor) constraint).getValidator();

                if (value instanceof List) {
                    validateList(context, constraint, validator, (List) value);

                } else if (value instanceof Iterable) {
                    validateIterable(context, constraint, validator, (Iterable) value);

                } else if (value instanceof Map && descriptor.getTypeArgumentIndex() == 0) {
                    validateMapKeys(context, constraint, validator, (Map<?, ?>) value);

                } else if (value instanceof Map) {
                    validateMapValues(context, constraint, validator, (Map<?, ?>) value);

                } else if (value instanceof Optional) {
                    validateOptional(context, constraint, validator, (Optional) value);
                }
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <T> void validateList(
            final MinijaxConstraintValidatorContext<T> context,
            final ConstraintDescriptor constraint,
            final ConstraintValidator validator,
            final List list) {

        for (int i = 0; i < list.size(); i++) {
            if (!validator.isValid(list.get(i), context)) {
                context.buildViolation(constraint, list.get(i), "[" + i + "].<list element>");
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <T> void validateIterable(
            final MinijaxConstraintValidatorContext<T> context,
            final ConstraintDescriptor constraint,
            final ConstraintValidator validator,
            final Iterable iterable) {

        for (final Object element : iterable) {
            if (!validator.isValid(element, context)) {
                context.buildViolation(constraint, element, "[].<iterable element>");
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <T> void validateMapKeys(
            final MinijaxConstraintValidatorContext<T> context,
            final ConstraintDescriptor constraint,
            final ConstraintValidator validator,
            final Map<?, ?> map) {

        for (final Object element : map.keySet()) {
            if (!validator.isValid(element, context)) {
                context.buildViolation(constraint, element, "<K>[].<map key>");
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <T> void validateMapValues(
            final MinijaxConstraintValidatorContext<T> context,
            final ConstraintDescriptor constraint,
            final ConstraintValidator validator,
            final Map<?, ?> map) {

        for (final Entry<?, ?> entry : map.entrySet()) {
            if (!validator.isValid(entry.getValue(), context)) {
                context.buildViolation(constraint, entry.getValue(), "[" + entry.getKey() + "].<map value>");
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <T> void validateOptional(
            final MinijaxConstraintValidatorContext<T> context,
            final ConstraintDescriptor constraint,
            final ConstraintValidator validator,
            final Optional optional) {

        if (optional.isPresent() && !validator.isValid(optional.get(), context)) {
            context.buildViolation(constraint, optional.get());
        }
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
