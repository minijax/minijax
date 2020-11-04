package org.minijax.client;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.core.GenericType;
import org.junit.jupiter.api.Test;

class ConversionUtilsTest {

    @Test
    void testConvertNull() {
        assertNull(ConversionUtils.convertToType(null, null, Object.class));
        assertNull(ConversionUtils.convertToGenericType(null, null, new GenericType<Object>() {
        }));
    }
}
