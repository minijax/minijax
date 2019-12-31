package org.minijax.validation;

import javax.validation.ClockProvider;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.Validator;
import javax.validation.ValidatorContext;
import javax.validation.ValidatorFactory;

public class MinijaxValidatorFactory implements ValidatorFactory {
    private final MinijaxValidator validator;
    private final MinijaxValidatorContext validatorContext;

    public MinijaxValidatorFactory() {
        validator = new MinijaxValidator();
        validatorContext = new MinijaxValidatorContext(validator);
    }

    @Override
    public Validator getValidator() {
        return validator;
    }

    @Override
    public ValidatorContext usingContext() {
        return validatorContext;
    }

    @Override
    public MessageInterpolator getMessageInterpolator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TraversableResolver getTraversableResolver() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConstraintValidatorFactory getConstraintValidatorFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParameterNameProvider getParameterNameProvider() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClockProvider getClockProvider() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(final Class<T> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }
}
