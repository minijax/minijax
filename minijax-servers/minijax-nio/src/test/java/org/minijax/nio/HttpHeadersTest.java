package org.minijax.nio;

import static java.util.Arrays.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

import org.junit.jupiter.api.Test;

class HttpHeadersTest {

    @Test
    void testBasic() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add("X-Foo", "bar");

        final MinijaxNioHttpHeaders headers = new MinijaxNioHttpHeaders(map);
        assertEquals(map, headers.getRequestHeaders());
        assertEquals("bar", headers.getHeaderString("X-Foo"));
        assertEquals(Collections.singletonList("bar"), headers.getRequestHeader("X-Foo"));
    }

    @Test
    void testMultiple() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add("X-Foo", "bar");
        map.add("X-Foo", "baz");

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertEquals(Arrays.asList("bar", "baz"), httpHeaders.getRequestHeader("X-Foo"));

        final MultivaluedMap<String, String> multiMap = httpHeaders.getRequestHeaders();
        assertNotNull(multiMap);
        assertEquals(multiMap, httpHeaders.getRequestHeaders());
    }

    @Test
    void testAccept() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add(HttpHeaders.ACCEPT, "text/plain");

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertEquals(Collections.singletonList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
        assertEquals(Collections.singletonList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
    }

    @Test
    void testAcceptLanguage() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add(HttpHeaders.ACCEPT_LANGUAGE, "en-US");

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertEquals(Collections.singletonList(Locale.US), httpHeaders.getAcceptableLanguages());
        assertEquals(Collections.singletonList(Locale.US), httpHeaders.getAcceptableLanguages());
    }

    @Test
    void testMediaType() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add(HttpHeaders.CONTENT_TYPE, "text/plain");

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertEquals(MediaType.TEXT_PLAIN_TYPE, httpHeaders.getMediaType());
    }

    @Test
    void testNullMediaType() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertNull(httpHeaders.getMediaType());
    }

    @Test
    void testLanguage() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add(HttpHeaders.CONTENT_LANGUAGE, "en-US");

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertEquals(Locale.US, httpHeaders.getLanguage());
    }

    @Test
    void testNullLanguage() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertNull(httpHeaders.getLanguage());
    }

    @Test
    void testContentLength() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add(HttpHeaders.CONTENT_LENGTH, "1024");

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertEquals(1024, httpHeaders.getLength());
    }

    @Test
    void testNullContentLength() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    void testInvalidContentLength() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add(HttpHeaders.CONTENT_LENGTH, "x");

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    void testCookies() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add(HttpHeaders.COOKIE, "k=v");

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        final Map<String, Cookie> cookies = httpHeaders.getCookies();
        assertNotNull(cookies);
        assertEquals(1, cookies.size());
        assertEquals("v", cookies.get("k").getValue());
        assertEquals(cookies, httpHeaders.getCookies());
    }

    @Test
    void testDate() {
        assertThrows(UnsupportedOperationException.class, () -> {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        httpHeaders.getDate();
    });
    }
}
