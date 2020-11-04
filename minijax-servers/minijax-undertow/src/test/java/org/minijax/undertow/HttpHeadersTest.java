package org.minijax.undertow;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;

import org.junit.jupiter.api.Test;

import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

class HttpHeadersTest {

    @Test
    void testBasic() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.CONTENT_TYPE, "text/plain");

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertEquals("text/plain", httpHeaders.getHeaderString("Content-Type"));
    }

    @Test
    void testMultiple() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(new HttpString("X-Foo"), "bar");
        headerMap.add(new HttpString("X-Foo"), "baz");

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertEquals(Arrays.asList("bar", "baz"), httpHeaders.getRequestHeader("X-Foo"));

        final MultivaluedMap<String, String> multiMap = httpHeaders.getRequestHeaders();
        assertNotNull(multiMap);
        assertEquals(multiMap, httpHeaders.getRequestHeaders());
    }

    @Test
    void testAccept() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.ACCEPT, "text/plain");

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertEquals(Collections.singletonList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
        assertEquals(Collections.singletonList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
    }

    @Test
    void testAcceptLanguage() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.ACCEPT_LANGUAGE, "en-US");

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertEquals(Collections.singletonList(Locale.US), httpHeaders.getAcceptableLanguages());
        assertEquals(Collections.singletonList(Locale.US), httpHeaders.getAcceptableLanguages());
    }

    @Test
    void testMediaType() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.CONTENT_TYPE, "text/plain");

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertEquals(MediaType.TEXT_PLAIN_TYPE, httpHeaders.getMediaType());
    }

    @Test
    void testNullMediaType() throws Exception {
        final HeaderMap headerMap = new HeaderMap();

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertNull(httpHeaders.getMediaType());
    }

    @Test
    void testLanguage() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.CONTENT_LANGUAGE, "en-US");

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertEquals(Locale.US, httpHeaders.getLanguage());
    }

    @Test
    void testNullLanguage() throws Exception {
        final HeaderMap headerMap = new HeaderMap();

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertNull(httpHeaders.getLanguage());
    }

    @Test
    void testContentLength() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.CONTENT_LENGTH, "1024");

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertEquals(1024, httpHeaders.getLength());
    }

    @Test
    void testNullContentLength() throws Exception {
        final HeaderMap headerMap = new HeaderMap();

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    void testInvalidContentLength() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.CONTENT_LENGTH, "x");

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    void testCookies() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.COOKIE, "k=v");

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        final Map<String, Cookie> cookies = httpHeaders.getCookies();
        assertNotNull(cookies);
        assertEquals(1, cookies.size());
        assertEquals("v", cookies.get("k").getValue());
        assertEquals(cookies, httpHeaders.getCookies());
    }

    @Test
    void testDate() {
        assertThrows(UnsupportedOperationException.class, () -> {
        final HeaderMap headerMap = new HeaderMap();
        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        httpHeaders.getDate();
    });
    }
}
