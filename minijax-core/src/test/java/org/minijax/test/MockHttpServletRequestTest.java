package org.minijax.test;

import static org.junit.Assert.*;

import java.net.URI;

import org.junit.Test;

public class MockHttpServletRequestTest {

    @Test
    public void testPathInfo() {
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", URI.create("/hello"));
        assertEquals("/hello", request.getPathInfo());
        assertEquals("/hello", request.getRequestURI());
    }

    @Test
    public void testAttributes() {
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", URI.create("/hello"));
        assertNull(request.getAttribute(null));
        assertNull(request.getAttributeNames());
    }
}
