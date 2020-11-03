package org.minijax.validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ValidationProviderTest {
    private MinijaxValidationProvider provider;

    @BeforeEach
    public void setUp() {
        provider = new MinijaxValidationProvider();
    }

    @Test
    public void testBuildValidatorFactory() {
        assertThrows(UnsupportedOperationException.class, () -> provider.buildValidatorFactory(null));
    }

    @Test
    public void testIgnoreXmlConfiguration() {
        assertThrows(UnsupportedOperationException.class, () -> provider.ignoreXmlConfiguration());
    }

    @Test
    public void testMessageInterpolator() {
        assertThrows(UnsupportedOperationException.class, () -> provider.messageInterpolator(null));
    }

    @Test
    public void testTraversableResolver() {
        assertThrows(UnsupportedOperationException.class, () -> provider.traversableResolver(null));
    }

    @Test
    public void testConstraintValidatorFactory() {
        assertThrows(UnsupportedOperationException.class, () -> provider.constraintValidatorFactory(null));
    }

    @Test
    public void testParameterNameProvider() {
        assertThrows(UnsupportedOperationException.class, () -> provider.parameterNameProvider(null));
    }

    @Test
    public void testClockProvider() {
        assertThrows(UnsupportedOperationException.class, () -> provider.clockProvider(null));
    }

    @Test
    public void testAddValueExtractor() {
        assertThrows(UnsupportedOperationException.class, () -> provider.addValueExtractor(null));
    }

    @Test
    public void testAddMapping() {
        assertThrows(UnsupportedOperationException.class, () -> provider.addMapping(null));
    }

    @Test
    public void testAddProperty() {
        assertThrows(UnsupportedOperationException.class, () -> provider.addProperty(null, null));
    }

    @Test
    public void testGetDefaultMessageInterpolator() {
        assertThrows(UnsupportedOperationException.class, () -> provider.getDefaultMessageInterpolator());
    }

    @Test
    public void testGetDefaultTraversableResolver() {
        assertThrows(UnsupportedOperationException.class, () -> provider.getDefaultTraversableResolver());
    }

    @Test
    public void testGetDefaultConstraintValidatorFactory() {
        assertThrows(UnsupportedOperationException.class, () -> provider.getDefaultConstraintValidatorFactory());
    }

    @Test
    public void testGetDefaultParameterNameProvider() {
        assertThrows(UnsupportedOperationException.class, () -> provider.getDefaultParameterNameProvider());
    }

    @Test
    public void testGetDefaultClockProvider() {
        assertThrows(UnsupportedOperationException.class, () -> provider.getDefaultClockProvider());
    }

    @Test
    public void testGetBootstrapConfiguration() {
        assertThrows(UnsupportedOperationException.class, () -> provider.getBootstrapConfiguration());
    }
}
