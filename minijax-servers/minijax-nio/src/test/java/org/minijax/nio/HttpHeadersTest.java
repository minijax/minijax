package org.minijax.nio;

import static java.util.Arrays.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

public class HttpHeadersTest {

    @Test
    public void testBasic() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add("X-Foo", "bar");

        final MinijaxNioHttpHeaders headers = new MinijaxNioHttpHeaders(map);
        assertEquals(map, headers.getRequestHeaders());
        assertEquals("bar", headers.getHeaderString("X-Foo"));
        assertEquals(asList("bar"), headers.getRequestHeader("X-Foo"));
    }

    @Test
    public void testMultiple() throws Exception {
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
    public void testAccept() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add(HttpHeaders.ACCEPT, "text/plain");

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertEquals(Arrays.asList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
        assertEquals(Arrays.asList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
    }

    @Test
    public void testAcceptLanguage() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add(HttpHeaders.ACCEPT_LANGUAGE, "en-US");

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertEquals(Arrays.asList(Locale.US), httpHeaders.getAcceptableLanguages());
        assertEquals(Arrays.asList(Locale.US), httpHeaders.getAcceptableLanguages());
    }

    @Test
    public void testMediaType() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add(HttpHeaders.CONTENT_TYPE, "text/plain");

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertEquals(MediaType.TEXT_PLAIN_TYPE, httpHeaders.getMediaType());
    }

    @Test
    public void testNullMediaType() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertNull(httpHeaders.getMediaType());
    }

    @Test
    public void testLanguage() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add(HttpHeaders.CONTENT_LANGUAGE, "en-US");

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertEquals(Locale.US, httpHeaders.getLanguage());
    }

    @Test
    public void testNullLanguage() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertNull(httpHeaders.getLanguage());
    }

    @Test
    public void testContentLength() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add(HttpHeaders.CONTENT_LENGTH, "1024");

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertEquals(1024, httpHeaders.getLength());
    }

    @Test
    public void testNullContentLength() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    public void testInvalidContentLength() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add(HttpHeaders.CONTENT_LENGTH, "x");

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    public void testCookies() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add(HttpHeaders.COOKIE, "k=v");

        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        final Map<String, Cookie> cookies = httpHeaders.getCookies();
        assertNotNull(cookies);
        assertEquals(1, cookies.size());
        assertEquals("v", cookies.get("k").getValue());
        assertEquals(cookies, httpHeaders.getCookies());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDate() {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        final MinijaxNioHttpHeaders httpHeaders = new MinijaxNioHttpHeaders(map);
        httpHeaders.getDate();
    }
}
