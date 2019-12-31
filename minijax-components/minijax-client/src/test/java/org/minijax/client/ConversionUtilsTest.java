package org.minijax.client;

import static org.junit.Assert.*;

import javax.ws.rs.core.GenericType;

import org.junit.Test;

public class ConversionUtilsTest {

    @Test
    public void testConvertNull() {
        assertNull(ConversionUtils.convertApacheToJax(null, Object.class));
        assertNull(ConversionUtils.convertToGenericType(null, new GenericType<Object>() { }));
    }
}
