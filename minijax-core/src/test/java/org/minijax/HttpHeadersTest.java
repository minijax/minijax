package org.minijax;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.Arrays;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;
import org.minijax.test.MockHttpServletRequest;

public class HttpHeadersTest {

    @Test
    public void testMultipleHeaders() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.add("a", "b");
        headers.add("a", "c");

        final MockHttpServletRequest request = new MockHttpServletRequest("GET", URI.create("/"), headers, null, null);

        final MinijaxHttpHeaders httpHeaders = new MinijaxHttpHeaders(request);
        assertEquals(Arrays.asList("b", "c"), httpHeaders.getRequestHeader("a"));
    }

    @Test
    public void testLanguageMissing() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", URI.create("/"), headers, null, null);
        final MinijaxHttpHeaders httpHeaders = new MinijaxHttpHeaders(request);
        assertNull(httpHeaders.getLanguage());
    }

    @Test
    public void testLanguage() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.add("Content-Language", "en-US");
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", URI.create("/"), headers, null, null);
        final MinijaxHttpHeaders httpHeaders = new MinijaxHttpHeaders(request);
        assertEquals("en-US", httpHeaders.getLanguage().toLanguageTag());
    }

    @Test
    public void testContentLengthMissing() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", URI.create("/"), headers, null, null);
        final MinijaxHttpHeaders httpHeaders = new MinijaxHttpHeaders(request);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    public void testContentLengthMalformed() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.add("Content-Length", "x");
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", URI.create("/"), headers, null, null);
        final MinijaxHttpHeaders httpHeaders = new MinijaxHttpHeaders(request);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    public void testContentLength() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.add("Content-Length", "1024");
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", URI.create("/"), headers, null, null);
        final MinijaxHttpHeaders httpHeaders = new MinijaxHttpHeaders(request);
        assertEquals(1024, httpHeaders.getLength());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDate() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", URI.create("/"), headers, null, null);
        final MinijaxHttpHeaders httpHeaders = new MinijaxHttpHeaders(request);
        httpHeaders.getDate();
    }
}
