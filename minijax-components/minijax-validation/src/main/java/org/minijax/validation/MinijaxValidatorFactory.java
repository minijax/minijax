package org.minijax.validation;

import jakarta.validation.ClockProvider;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.ParameterNameProvider;
import jakarta.validation.TraversableResolver;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorContext;
import jakarta.validation.ValidatorFactory;

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
