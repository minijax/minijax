package org.minijax.rs.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

import org.junit.jupiter.api.Test;

class HttpHeadersTest {

    @Test
    void testMultipleHeaders() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.add("a", "b");
        headers.add("a", "c");

        final MinijaxTestHttpHeaders httpHeaders = new MinijaxTestHttpHeaders(headers);
        assertEquals(Arrays.asList("b", "c"), httpHeaders.getRequestHeader("a"));
    }

    @Test
    void testLanguageMissing() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        final MinijaxTestHttpHeaders httpHeaders = new MinijaxTestHttpHeaders(headers);
        assertNull(httpHeaders.getLanguage());
    }

    @Test
    void testLanguage() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.add("Content-Language", "en-US");

        final MinijaxTestHttpHeaders httpHeaders = new MinijaxTestHttpHeaders(headers);
        assertEquals("en-US", httpHeaders.getLanguage().toLanguageTag());
    }

    @Test
    void testContentLengthMissing() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        final MinijaxTestHttpHeaders httpHeaders = new MinijaxTestHttpHeaders(headers);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    void testContentLengthMalformed() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.add("Content-Length", "x");

        final MinijaxTestHttpHeaders httpHeaders = new MinijaxTestHttpHeaders(headers);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    void testContentLength() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.add("Content-Length", "1024");

        final MinijaxTestHttpHeaders httpHeaders = new MinijaxTestHttpHeaders(headers);
        assertEquals(1024, httpHeaders.getLength());
    }

    @Test
    void testDate() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
            final MinijaxTestHttpHeaders httpHeaders = new MinijaxTestHttpHeaders(headers);
            httpHeaders.getDate();
        });
    }

    @Test
    void testSimpleCookie() {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add("Cookie", "a=b");

        final MinijaxTestHttpHeaders headers = new MinijaxTestHttpHeaders(map);
        assertEquals("b", headers.getCookies().get("a").getValue());
    }

    @Test
    void testSemicolonCookie() {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add("Cookie", "a=b; c=d");

        final MinijaxTestHttpHeaders headers = new MinijaxTestHttpHeaders(map);
        assertEquals("b", headers.getCookies().get("a").getValue());
        assertEquals("d", headers.getCookies().get("c").getValue());
    }
}
