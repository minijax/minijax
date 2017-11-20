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
        System.out.println("messageInterpolator");
        return this;
    }

    @Override
    public MinijaxValidatorContext traversableResolver(final TraversableResolver traversableResolver) {
        System.out.println("traversableResolver");
        return this;
    }

    @Override
    public MinijaxValidatorContext constraintValidatorFactory(final ConstraintValidatorFactory factory) {
        System.out.println("constraintValidatorFactory");
        return this;
    }

    @Override
    public MinijaxValidatorContext parameterNameProvider(final ParameterNameProvider parameterNameProvider) {
        System.out.println("parameterNameProvider");
        return this;
    }

    @Override
    public MinijaxValidatorContext clockProvider(final ClockProvider clockProvider) {
        System.out.println("clockProvider");
        return this;
    }

    @Override
    public MinijaxValidatorContext addValueExtractor(final ValueExtractor<?> extractor) {
        System.out.println("addValueExtractor");
        return this;
    }

    @Override
    public MinijaxValidator getValidator() {
        return validator;
    }
}
