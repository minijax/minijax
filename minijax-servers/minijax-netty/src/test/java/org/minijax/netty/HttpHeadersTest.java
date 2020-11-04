package org.minijax.netty;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;

import org.junit.jupiter.api.Test;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

class HttpHeadersTest {

    @Test
    void testBasic() throws Exception {
        final HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("Content-Type", "text/plain");

        final HttpRequest request = mock(HttpRequest.class);
        when(request.headers()).thenReturn(headers);

        final MinijaxNettyHttpHeaders httpHeaders = new MinijaxNettyHttpHeaders(request);
        assertEquals("text/plain", httpHeaders.getHeaderString("Content-Type"));
    }

    @Test
    void testMultiple() throws Exception {
        final HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("X-Foo", "bar");
        headers.add("X-Foo", "baz");

        final HttpRequest request = mock(HttpRequest.class);
        when(request.headers()).thenReturn(headers);

        final MinijaxNettyHttpHeaders httpHeaders = new MinijaxNettyHttpHeaders(request);
        assertEquals(Arrays.asList("bar", "baz"), httpHeaders.getRequestHeader("X-Foo"));

        final MultivaluedMap<String, String> multiMap = httpHeaders.getRequestHeaders();
        assertNotNull(multiMap);
        assertEquals(multiMap, httpHeaders.getRequestHeaders());
    }

    @Test
    void testAccept() throws Exception {
        final HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("Accept", "text/plain");

        final HttpRequest request = mock(HttpRequest.class);
        when(request.headers()).thenReturn(headers);

        final MinijaxNettyHttpHeaders httpHeaders = new MinijaxNettyHttpHeaders(request);
        assertEquals(Collections.singletonList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
        assertEquals(Collections.singletonList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
    }

    @Test
    void testAcceptLanguage() throws Exception {
        final HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("Accept-Language", "en-US");

        final HttpRequest request = mock(HttpRequest.class);
        when(request.headers()).thenReturn(headers);

        final MinijaxNettyHttpHeaders httpHeaders = new MinijaxNettyHttpHeaders(request);
        assertEquals(Collections.singletonList(Locale.US), httpHeaders.getAcceptableLanguages());
        assertEquals(Collections.singletonList(Locale.US), httpHeaders.getAcceptableLanguages());
    }

    @Test
    void testMediaType() throws Exception {
        final HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("Content-Type", "text/plain");

        final HttpRequest request = mock(HttpRequest.class);
        when(request.headers()).thenReturn(headers);

        final MinijaxNettyHttpHeaders httpHeaders = new MinijaxNettyHttpHeaders(request);
        assertEquals(MediaType.TEXT_PLAIN_TYPE, httpHeaders.getMediaType());
    }

    @Test
    void testNullMediaType() throws Exception {
        final HttpHeaders headers = new DefaultHttpHeaders();

        final HttpRequest request = mock(HttpRequest.class);
        when(request.headers()).thenReturn(headers);

        final MinijaxNettyHttpHeaders httpHeaders = new MinijaxNettyHttpHeaders(request);
        assertNull(httpHeaders.getMediaType());
    }

    @Test
    void testLanguage() throws Exception {
        final HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("Content-Language", "en-US");

        final HttpRequest request = mock(HttpRequest.class);
        when(request.headers()).thenReturn(headers);

        final MinijaxNettyHttpHeaders httpHeaders = new MinijaxNettyHttpHeaders(request);
        assertEquals(Locale.US, httpHeaders.getLanguage());
    }

    @Test
    void testNullLanguage() throws Exception {
        final HttpHeaders headers = new DefaultHttpHeaders();

        final HttpRequest request = mock(HttpRequest.class);
        when(request.headers()).thenReturn(headers);

        final MinijaxNettyHttpHeaders httpHeaders = new MinijaxNettyHttpHeaders(request);
        assertNull(httpHeaders.getLanguage());
    }

    @Test
    void testContentLength() throws Exception {
        final HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("Content-Length", "1024");

        final HttpRequest request = mock(HttpRequest.class);
        when(request.headers()).thenReturn(headers);

        final MinijaxNettyHttpHeaders httpHeaders = new MinijaxNettyHttpHeaders(request);
        assertEquals(1024, httpHeaders.getLength());
    }

    @Test
    void testNullContentLength() throws Exception {
        final HttpHeaders headers = new DefaultHttpHeaders();

        final HttpRequest request = mock(HttpRequest.class);
        when(request.headers()).thenReturn(headers);

        final MinijaxNettyHttpHeaders httpHeaders = new MinijaxNettyHttpHeaders(request);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    void testInvalidContentLength() throws Exception {
        final HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("Content-Length", "x");

        final HttpRequest request = mock(HttpRequest.class);
        when(request.headers()).thenReturn(headers);

        final MinijaxNettyHttpHeaders httpHeaders = new MinijaxNettyHttpHeaders(request);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    void testCookies() throws Exception {
        final HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("Cookie", "k=v");

        final HttpRequest request = mock(HttpRequest.class);
        when(request.headers()).thenReturn(headers);

        final MinijaxNettyHttpHeaders httpHeaders = new MinijaxNettyHttpHeaders(request);
        final Map<String, Cookie> cookies = httpHeaders.getCookies();
        assertNotNull(cookies);
        assertEquals(1, cookies.size());
        assertEquals("v", cookies.get("k").getValue());
        assertEquals(cookies, httpHeaders.getCookies());
    }

    @Test
    void testDate() {
        assertThrows(UnsupportedOperationException.class, () -> {
        final HttpRequest request = mock(HttpRequest.class);
        final MinijaxNettyHttpHeaders httpHeaders = new MinijaxNettyHttpHeaders(request);
        httpHeaders.getDate();
    });
    }
}
