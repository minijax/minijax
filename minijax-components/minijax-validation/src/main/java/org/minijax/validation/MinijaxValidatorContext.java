package org.minijax.validation;

import jakarta.validation.ClockProvider;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.ParameterNameProvider;
import jakarta.validation.TraversableResolver;
import jakarta.validation.ValidatorContext;
import jakarta.validation.valueextraction.ValueExtractor;

public class MinijaxValidatorContext implements ValidatorContext {
    private final MinijaxValidator validator;

    public MinijaxValidatorContext(final MinijaxValidator validator) {
        this.validator = validator;
    }

    @Override
    public MinijaxValidatorContext messageInterpolator(final MessageInterpolator messageInterpolator) {
        return this;
    }

    @Override
    public MinijaxValidatorContext traversableResolver(final TraversableResolver traversableResolver) {
        return this;
    }

    @Override
    public MinijaxValidatorContext constraintValidatorFactory(final ConstraintValidatorFactory factory) {
        return this;
    }

    @Override
    public MinijaxValidatorContext parameterNameProvider(final ParameterNameProvider parameterNameProvider) {
        return this;
    }

    @Override
    public MinijaxValidatorContext clockProvider(final ClockProvider clockProvider) {
        return this;
    }

    @Override
    public MinijaxValidatorContext addValueExtractor(final ValueExtractor<?> extractor) {
        return this;
    }

    @Override
    public MinijaxValidator getValidator() {
        return validator;
    }
}
