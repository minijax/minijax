package org.minijax.validator;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;

public class MinijaxConstraintViolation<T> implements ConstraintViolation<T> {
    private final T rootBean;
    private final Path propertyPath;
    private final ConstraintDescriptor<?> constraintDescriptor;
    private final String message;

    public MinijaxConstraintViolation(final T rootBean, final Path propertyPath, final ConstraintDescriptor<?> constraintDescriptor) {
        this.rootBean = rootBean;
        this.propertyPath = propertyPath;
        this.constraintDescriptor = constraintDescriptor;
        message = MinijaxMessageInterpolator.generateMessage(constraintDescriptor.getMessageTemplate(), constraintDescriptor.getAnnotation());
    }

    @Override
    public T getRootBean() {
        return rootBean;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<T> getRootBeanClass() {
        return (Class<T>) rootBean.getClass();
    }

    @Override
    public Path getPropertyPath() {
        return propertyPath;
    }

    @Override
    public ConstraintDescriptor<?> getConstraintDescriptor() {
        return constraintDescriptor;
    }

    @Override
    public String getMessageTemplate() {
        return constraintDescriptor.getMessageTemplate();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return propertyPath + " " + message;
    }

    /*
     * Not implemented
     */

    @Override
    public Object getLeafBean() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] getExecutableParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getExecutableReturnValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getInvalidValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <U> U unwrap(final Class<U> type) {
        throw new UnsupportedOperationException();
    }
}
