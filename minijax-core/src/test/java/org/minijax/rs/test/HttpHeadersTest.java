package org.minijax.rs.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

import org.junit.jupiter.api.Test;

public class HttpHeadersTest {

    @Test
    public void testMultipleHeaders() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.add("a", "b");
        headers.add("a", "c");

        final MinijaxTestHttpHeaders httpHeaders = new MinijaxTestHttpHeaders(headers, null);
        assertEquals(Arrays.asList("b", "c"), httpHeaders.getRequestHeader("a"));
    }

    @Test
    public void testLanguageMissing() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        final MinijaxTestHttpHeaders httpHeaders = new MinijaxTestHttpHeaders(headers, null);
        assertNull(httpHeaders.getLanguage());
    }

    @Test
    public void testLanguage() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.add("Content-Language", "en-US");

        final MinijaxTestHttpHeaders httpHeaders = new MinijaxTestHttpHeaders(headers, null);
        assertEquals("en-US", httpHeaders.getLanguage().toLanguageTag());
    }

    @Test
    public void testContentLengthMissing() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        final MinijaxTestHttpHeaders httpHeaders = new MinijaxTestHttpHeaders(headers, null);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    public void testContentLengthMalformed() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.add("Content-Length", "x");

        final MinijaxTestHttpHeaders httpHeaders = new MinijaxTestHttpHeaders(headers, null);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    public void testContentLength() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.add("Content-Length", "1024");

        final MinijaxTestHttpHeaders httpHeaders = new MinijaxTestHttpHeaders(headers, null);
        assertEquals(1024, httpHeaders.getLength());
    }

    @Test
    public void testDate() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
            final MinijaxTestHttpHeaders httpHeaders = new MinijaxTestHttpHeaders(headers, null);
            httpHeaders.getDate();
        });
    }

    @Test
    public void testSimpleCookie() {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add("Cookie", "a=b");

        final MinijaxTestHttpHeaders headers = new MinijaxTestHttpHeaders(map, null);
        assertEquals("b", headers.getCookies().get("a").getValue());
    }

    @Test
    public void testSemicolonCookie() {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add("Cookie", "a=b; c=d");

        final MinijaxTestHttpHeaders headers = new MinijaxTestHttpHeaders(map, null);
        assertEquals("b", headers.getCookies().get("a").getValue());
        assertEquals("d", headers.getCookies().get("c").getValue());
    }
}
