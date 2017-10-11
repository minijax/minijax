package org.minijax;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;
import org.minijax.servlet.MockHttpServletRequest;

public class ServletRequestContextTest {

    @Test
    public void testHeaders() throws IOException {
        final MultivaluedMap<String, String> mockHeaders = new MultivaluedHashMap<>();
        mockHeaders.add("a", "b");

        final MockHttpServletRequest mockRequest = new MockHttpServletRequest(mockHeaders, null, "GET", "/", null);

        try (final MinijaxServletRequestContext context = new MinijaxServletRequestContext(mockRequest)) {
            final MultivaluedMap<String, String> headers = context.getHeaders();
            assertEquals("b", headers.get("a").get(0));

            // Assert that same cached object
            assertTrue(headers == context.getHeaders());
        }
    }

    @Test
    public void testCookies() throws IOException {
        final javax.servlet.http.Cookie[] mockCookies = new javax.servlet.http.Cookie[] { new javax.servlet.http.Cookie("a", "b") };
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest(null, mockCookies, "GET", "/", null);

        try (final MinijaxServletRequestContext context = new MinijaxServletRequestContext(mockRequest)) {
            final Map<String, Cookie> cookies = context.getCookies();
            assertEquals("b", cookies.get("a").getValue());

            // Assert that same cached object
            assertTrue(cookies == context.getCookies());
        }
    }

    @Test(expected = BadRequestException.class)
    public void testFormMissingContentType() throws IOException {
        final String mockContentBody = "a=b";

        final MockHttpServletRequest mockRequest = new MockHttpServletRequest(null, null, "POST", "/", mockContentBody);

        try (final MinijaxServletRequestContext context = new MinijaxServletRequestContext(mockRequest)) {
            context.getForm();
        }
    }

    @Test(expected = BadRequestException.class)
    public void testFormUnknownContentType() throws IOException {
        final MultivaluedMap<String, String> mockHeaders = new MultivaluedHashMap<>();
        mockHeaders.add("Content-Type", "text/plain");

        final String mockContentBody = "a=b";

        final MockHttpServletRequest mockRequest = new MockHttpServletRequest(mockHeaders, null, "POST", "/", mockContentBody);

        try (final MinijaxServletRequestContext context = new MinijaxServletRequestContext(mockRequest)) {
            context.getForm();
        }
    }

    @Test
    public void testUrlEncodedForm() throws IOException {
        final MultivaluedMap<String, String> mockHeaders = new MultivaluedHashMap<>();
        mockHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        final String mockContentBody = "a=b";

        final MockHttpServletRequest mockRequest = new MockHttpServletRequest(mockHeaders, null, "POST", "/", mockContentBody);

        try (final MinijaxServletRequestContext context = new MinijaxServletRequestContext(mockRequest)) {
            final MinijaxForm form = context.getForm();
            assertTrue(form instanceof MinijaxUrlEncodedForm);
            assertEquals("b", form.getString("a"));

            // Assert that same cached object
            assertTrue(form == context.getForm());
        }
    }

    @Test
    public void testMultipartForm() throws IOException {
        final MultivaluedMap<String, String> mockHeaders = new MultivaluedHashMap<>();
        mockHeaders.add("Content-Type", "multipart/form-data");

        final String mockContentBody =
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ\n" +
                "Content-Disposition: form-data; name=\"a\"\n" +
                "\n" +
                "b\n" +
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ";

        final MockHttpServletRequest mockRequest = new MockHttpServletRequest(mockHeaders, null, "POST", "/", mockContentBody);

        try (final MinijaxServletRequestContext context = new MinijaxServletRequestContext(mockRequest)) {
            final MinijaxForm form = context.getForm();
            assertTrue(form instanceof MinijaxMultipartForm);
            assertEquals("b", form.getString("a"));

            // Assert that same cached object
            assertTrue(form == context.getForm());
        }
    }
}
