package org.minijax.validator;

import javax.validation.ClockProvider;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.ValidatorContext;
import javax.validation.valueextraction.ValueExtractor;

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
