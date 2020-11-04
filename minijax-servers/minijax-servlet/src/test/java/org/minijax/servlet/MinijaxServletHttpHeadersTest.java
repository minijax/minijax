package org.minijax.servlet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;

import org.junit.jupiter.api.Test;

class MinijaxServletHttpHeadersTest {

    @Test
    void testBasic() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(eq("Content-Type"))).thenReturn("text/plain");

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals("text/plain", httpHeaders.getHeaderString("Content-Type"));
    }

    @Test
    void testMultiple() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.singletonList("X-Foo")));
        when(request.getHeaders("X-Foo")).thenReturn(Collections.enumeration(Arrays.asList("bar", "baz")));

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals(Arrays.asList("bar", "baz"), httpHeaders.getRequestHeader("X-Foo"));

        final MultivaluedMap<String, String> multiMap = httpHeaders.getRequestHeaders();
        assertNotNull(multiMap);
        assertEquals(multiMap, httpHeaders.getRequestHeaders());
    }

    @Test
    void testAccept() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(eq("Accept"))).thenReturn("text/plain");

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals(Collections.singletonList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
        assertEquals(Collections.singletonList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
    }

    @Test
    void testAcceptLanguage() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(eq("Accept-Language"))).thenReturn("en-US");

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals(Collections.singletonList(Locale.US), httpHeaders.getAcceptableLanguages());
        assertEquals(Collections.singletonList(Locale.US), httpHeaders.getAcceptableLanguages());
    }

    @Test
    void testMediaType() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(eq("Content-Type"))).thenReturn("text/plain");

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals(MediaType.TEXT_PLAIN_TYPE, httpHeaders.getMediaType());
    }

    @Test
    void testNullMediaType() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertNull(httpHeaders.getMediaType());
    }

    @Test
    void testLanguage() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(eq("Content-Language"))).thenReturn("en-US");

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals(Locale.US, httpHeaders.getLanguage());
    }

    @Test
    void testNullLanguage() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertNull(httpHeaders.getLanguage());
    }

    @Test
    void testContentLength() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(eq("Content-Length"))).thenReturn("1024");

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals(1024, httpHeaders.getLength());
    }

    @Test
    void testNullContentLength() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    void testInvalidContentLength() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(eq("Content-Length"))).thenReturn("x");

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    void testCookies() throws Exception {
        final jakarta.servlet.http.Cookie[] servletCookies = new jakarta.servlet.http.Cookie[] {
                new jakarta.servlet.http.Cookie("k", "v") };

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(servletCookies);

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        final Map<String, Cookie> cookies = httpHeaders.getCookies();
        assertNotNull(cookies);
        assertEquals(1, cookies.size());
        assertEquals("v", cookies.get("k").getValue());
        assertEquals(cookies, httpHeaders.getCookies());
    }

    @Test
    void testDate() {
        assertThrows(UnsupportedOperationException.class, () -> {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        httpHeaders.getDate();
    });
    }
}
