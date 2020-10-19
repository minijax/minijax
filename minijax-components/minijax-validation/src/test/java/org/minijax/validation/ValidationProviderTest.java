package org.minijax.validation;

import org.junit.Before;
import org.junit.Test;

public class ValidationProviderTest {
    private MinijaxValidationProvider provider;

    @Before
    public void setUp() {
        provider = new MinijaxValidationProvider();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBuildValidatorFactory() {
        provider.buildValidatorFactory(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIgnoreXmlConfiguration() {
        provider.ignoreXmlConfiguration();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMessageInterpolator() {
        provider.messageInterpolator(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTraversableResolver() {
        provider.traversableResolver(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConstraintValidatorFactory() {
        provider.constraintValidatorFactory(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParameterNameProvider() {
        provider.parameterNameProvider(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClockProvider() {
        provider.clockProvider(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueExtractor() {
        provider.addValueExtractor(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddMapping() {
        provider.addMapping(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddProperty() {
        provider.addProperty(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDefaultMessageInterpolator() {
        provider.getDefaultMessageInterpolator();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDefaultTraversableResolver() {
        provider.getDefaultTraversableResolver();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDefaultConstraintValidatorFactory() {
        provider.getDefaultConstraintValidatorFactory();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDefaultParameterNameProvider() {
        provider.getDefaultParameterNameProvider();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDefaultClockProvider() {
        provider.getDefaultClockProvider();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetBootstrapConfiguration() {
        provider.getBootstrapConfiguration();
    }
}
