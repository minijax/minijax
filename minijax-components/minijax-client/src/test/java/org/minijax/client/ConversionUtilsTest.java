package org.minijax.client;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.core.GenericType;
import org.junit.jupiter.api.Test;

public class ConversionUtilsTest {

    @Test
    public void testConvertNull() {
        assertNull(ConversionUtils.convertToType(null, null, Object.class));
        assertNull(ConversionUtils.convertToGenericType(null, null, new GenericType<Object>() {
        }));
    }
}
