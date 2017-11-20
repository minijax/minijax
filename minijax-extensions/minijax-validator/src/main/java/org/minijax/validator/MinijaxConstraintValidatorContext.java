package org.minijax.validator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ClockProvider;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.minijax.validator.MinijaxPath.MinijaxContainerElementNode;
import org.minijax.validator.MinijaxPath.MinijaxPropertyNode;

public class MinijaxConstraintValidatorContext<T> implements ConstraintValidatorContext {
    private final T rootBean;
    private final List<Node> pathNodes;
    private final Set<ConstraintViolation<T>> result;

    public MinijaxConstraintValidatorContext(final T rootBean) {
        this.rootBean = rootBean;
        pathNodes = new ArrayList<>();
        result = new HashSet<>();
    }

    public T getRootBean() {
        return rootBean;
    }

    public void push(final PropertyDescriptor property) {
        pathNodes.add(new MinijaxPropertyNode(pathNodes.size(), property.getPropertyName()));
    }

    public void pop() {
        pathNodes.remove(pathNodes.size() - 1);
    }

    public void buildViolation(final ConstraintDescriptor<?> constraintDescriptor, final Object invalidValue) {
        final Path path = new MinijaxPath(new ArrayList<>(pathNodes));
        result.add(new MinijaxConstraintViolation<>(rootBean, path, invalidValue, constraintDescriptor));
    }

    public void buildViolation(final ConstraintDescriptor<?> constraintDescriptor, final Object invalidValue, final String elementName) {
        pathNodes.add(new MinijaxContainerElementNode(pathNodes.size(), elementName));
        final Path path = new MinijaxPath(new ArrayList<>(pathNodes));
        pop();
        result.add(new MinijaxConstraintViolation<>(rootBean, path, invalidValue, constraintDescriptor));
    }

    public Set<ConstraintViolation<T>> getResult() {
        return result;
    }

    @Override
    public void disableDefaultConstraintViolation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDefaultConstraintMessageTemplate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClockProvider getClockProvider() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConstraintViolationBuilder buildConstraintViolationWithTemplate(final String messageTemplate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <U> U unwrap(final Class<U> type) {
        throw new UnsupportedOperationException();
    }
}
