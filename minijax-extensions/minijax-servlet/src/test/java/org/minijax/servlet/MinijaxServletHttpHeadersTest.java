package org.minijax.servlet;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

public class MinijaxServletHttpHeadersTest {

    @Test
    public void testBasic() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(eq("Content-Type"))).thenReturn("text/plain");

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals("text/plain", httpHeaders.getHeaderString("Content-Type"));
    }

    @Test
    public void testMultiple() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(Arrays.asList("X-Foo")));
        when(request.getHeaders("X-Foo")).thenReturn(Collections.enumeration(Arrays.asList("bar", "baz")));

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals(Arrays.asList("bar", "baz"), httpHeaders.getRequestHeader("X-Foo"));

        final MultivaluedMap<String, String> multiMap = httpHeaders.getRequestHeaders();
        assertNotNull(multiMap);
        assertEquals(multiMap, httpHeaders.getRequestHeaders());
    }

    @Test
    public void testAccept() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(eq("Accept"))).thenReturn("text/plain");

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals(Arrays.asList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
        assertEquals(Arrays.asList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
    }

    @Test
    public void testAcceptLanguage() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(eq("Accept-Language"))).thenReturn("en-US");

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals(Arrays.asList(Locale.US), httpHeaders.getAcceptableLanguages());
        assertEquals(Arrays.asList(Locale.US), httpHeaders.getAcceptableLanguages());
    }

    @Test
    public void testMediaType() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(eq("Content-Type"))).thenReturn("text/plain");

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals(MediaType.TEXT_PLAIN_TYPE, httpHeaders.getMediaType());
    }

    @Test
    public void testNullMediaType() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertNull(httpHeaders.getMediaType());
    }

    @Test
    public void testLanguage() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(eq("Content-Language"))).thenReturn("en-US");

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals(Locale.US, httpHeaders.getLanguage());
    }

    @Test
    public void testNullLanguage() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertNull(httpHeaders.getLanguage());
    }

    @Test
    public void testContentLength() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(eq("Content-Length"))).thenReturn("1024");

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals(1024, httpHeaders.getLength());
    }

    @Test
    public void testNullContentLength() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    public void testInvalidContentLength() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(eq("Content-Length"))).thenReturn("x");

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    public void testCookies() throws Exception {
        final javax.servlet.http.Cookie[] servletCookies = new javax.servlet.http.Cookie[] {
                new javax.servlet.http.Cookie("k", "v") };

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(servletCookies);

        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        final Map<String, Cookie> cookies = httpHeaders.getCookies();
        assertNotNull(cookies);
        assertEquals(1, cookies.size());
        assertEquals("v", cookies.get("k").getValue());
        assertEquals(cookies, httpHeaders.getCookies());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDate() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final MinijaxServletHttpHeaders httpHeaders = new MinijaxServletHttpHeaders(request);
        httpHeaders.getDate();
    }
}
