package org.minijax.validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValidationProviderTest {
    private MinijaxValidationProvider provider;

    @BeforeEach
    public void setUp() {
        provider = new MinijaxValidationProvider();
    }

    @Test
    void testBuildValidatorFactory() {
        assertThrows(UnsupportedOperationException.class, () -> provider.buildValidatorFactory(null));
    }

    @Test
    void testIgnoreXmlConfiguration() {
        assertThrows(UnsupportedOperationException.class, () -> provider.ignoreXmlConfiguration());
    }

    @Test
    void testMessageInterpolator() {
        assertThrows(UnsupportedOperationException.class, () -> provider.messageInterpolator(null));
    }

    @Test
    void testTraversableResolver() {
        assertThrows(UnsupportedOperationException.class, () -> provider.traversableResolver(null));
    }

    @Test
    void testConstraintValidatorFactory() {
        assertThrows(UnsupportedOperationException.class, () -> provider.constraintValidatorFactory(null));
    }

    @Test
    void testParameterNameProvider() {
        assertThrows(UnsupportedOperationException.class, () -> provider.parameterNameProvider(null));
    }

    @Test
    void testClockProvider() {
        assertThrows(UnsupportedOperationException.class, () -> provider.clockProvider(null));
    }

    @Test
    void testAddValueExtractor() {
        assertThrows(UnsupportedOperationException.class, () -> provider.addValueExtractor(null));
    }

    @Test
    void testAddMapping() {
        assertThrows(UnsupportedOperationException.class, () -> provider.addMapping(null));
    }

    @Test
    void testAddProperty() {
        assertThrows(UnsupportedOperationException.class, () -> provider.addProperty(null, null));
    }

    @Test
    void testGetDefaultMessageInterpolator() {
        assertThrows(UnsupportedOperationException.class, () -> provider.getDefaultMessageInterpolator());
    }

    @Test
    void testGetDefaultTraversableResolver() {
        assertThrows(UnsupportedOperationException.class, () -> provider.getDefaultTraversableResolver());
    }

    @Test
    void testGetDefaultConstraintValidatorFactory() {
        assertThrows(UnsupportedOperationException.class, () -> provider.getDefaultConstraintValidatorFactory());
    }

    @Test
    void testGetDefaultParameterNameProvider() {
        assertThrows(UnsupportedOperationException.class, () -> provider.getDefaultParameterNameProvider());
    }

    @Test
    void testGetDefaultClockProvider() {
        assertThrows(UnsupportedOperationException.class, () -> provider.getDefaultClockProvider());
    }

    @Test
    void testGetBootstrapConfiguration() {
        assertThrows(UnsupportedOperationException.class, () -> provider.getBootstrapConfiguration());
    }
}
