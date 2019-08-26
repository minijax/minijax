package org.minijax.validation;

import java.io.InputStream;

import javax.validation.BootstrapConfiguration;
import javax.validation.ClockProvider;
import javax.validation.Configuration;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.ValidatorFactory;
import javax.validation.spi.BootstrapState;
import javax.validation.spi.ConfigurationState;
import javax.validation.spi.ValidationProvider;
import javax.validation.valueextraction.ValueExtractor;

public class MinijaxValidationProvider implements Configuration<MinijaxValidationProvider>, ValidationProvider<MinijaxValidationProvider> {

    @Override
    public MinijaxValidationProvider createSpecializedConfiguration(final BootstrapState state) {
        return this;
    }

    @Override
    public MinijaxValidationProvider createGenericConfiguration(final BootstrapState state) {
        return this;
    }

    @Override
    public ValidatorFactory buildValidatorFactory() {
        return new MinijaxValidatorFactory();
    }

    /*
     * Not implemented
     */

    @Override
    public ValidatorFactory buildValidatorFactory(final ConfigurationState configurationState) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxValidationProvider ignoreXmlConfiguration() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxValidationProvider messageInterpolator(final MessageInterpolator interpolator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxValidationProvider traversableResolver(final TraversableResolver resolver) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxValidationProvider constraintValidatorFactory(final ConstraintValidatorFactory constraintValidatorFactory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxValidationProvider parameterNameProvider(final ParameterNameProvider parameterNameProvider) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxValidationProvider clockProvider(final ClockProvider clockProvider) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxValidationProvider addValueExtractor(final ValueExtractor<?> extractor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxValidationProvider addMapping(final InputStream stream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxValidationProvider addProperty(final String name, final String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MessageInterpolator getDefaultMessageInterpolator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TraversableResolver getDefaultTraversableResolver() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConstraintValidatorFactory getDefaultConstraintValidatorFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParameterNameProvider getDefaultParameterNameProvider() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClockProvider getDefaultClockProvider() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BootstrapConfiguration getBootstrapConfiguration() {
        throw new UnsupportedOperationException();
    }
}
