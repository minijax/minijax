package org.minijax.undertow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;

public class HttpHeadersTest {

    @Test
    public void testBasic() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.CONTENT_TYPE, "text/plain");

        final HttpServerExchange exchange = mock(HttpServerExchange.class);
        when(exchange.getRequestMethod()).thenReturn(Methods.GET);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(exchange);
        assertEquals("text/plain", httpHeaders.getHeaderString("Content-Type"));
    }

    @Test
    public void testMultiple() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(new HttpString("X-Foo"), "bar");
        headerMap.add(new HttpString("X-Foo"), "baz");

        final HttpServerExchange exchange = mock(HttpServerExchange.class);
        when(exchange.getRequestMethod()).thenReturn(Methods.GET);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(exchange);
        assertEquals(Arrays.asList("bar", "baz"), httpHeaders.getRequestHeader("X-Foo"));

        final MultivaluedMap<String, String> multiMap = httpHeaders.getRequestHeaders();
        assertNotNull(multiMap);
        assertEquals(multiMap, httpHeaders.getRequestHeaders());
    }

    @Test
    public void testAccept() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.ACCEPT, "text/plain");

        final HttpServerExchange exchange = mock(HttpServerExchange.class);
        when(exchange.getRequestMethod()).thenReturn(Methods.GET);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(exchange);
        assertEquals(Arrays.asList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
        assertEquals(Arrays.asList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
    }

    @Test
    public void testAcceptLanguage() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.ACCEPT_LANGUAGE, "en-US");

        final HttpServerExchange exchange = mock(HttpServerExchange.class);
        when(exchange.getRequestMethod()).thenReturn(Methods.GET);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(exchange);
        assertEquals(Arrays.asList(Locale.US), httpHeaders.getAcceptableLanguages());
        assertEquals(Arrays.asList(Locale.US), httpHeaders.getAcceptableLanguages());
    }

    @Test
    public void testMediaType() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.CONTENT_TYPE, "text/plain");

        final HttpServerExchange exchange = mock(HttpServerExchange.class);
        when(exchange.getRequestMethod()).thenReturn(Methods.GET);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(exchange);
        assertEquals(MediaType.TEXT_PLAIN_TYPE, httpHeaders.getMediaType());
    }

    @Test
    public void testNullMediaType() throws Exception {
        final HeaderMap headerMap = new HeaderMap();

        final HttpServerExchange exchange = mock(HttpServerExchange.class);
        when(exchange.getRequestMethod()).thenReturn(Methods.GET);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(exchange);
        assertNull(httpHeaders.getMediaType());
    }

    @Test
    public void testLanguage() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.CONTENT_LANGUAGE, "en-US");

        final HttpServerExchange exchange = mock(HttpServerExchange.class);
        when(exchange.getRequestMethod()).thenReturn(Methods.GET);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(exchange);
        assertEquals(Locale.US, httpHeaders.getLanguage());
    }

    @Test
    public void testNullLanguage() throws Exception {
        final HeaderMap headerMap = new HeaderMap();

        final HttpServerExchange exchange = mock(HttpServerExchange.class);
        when(exchange.getRequestMethod()).thenReturn(Methods.GET);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(exchange);
        assertNull(httpHeaders.getLanguage());
    }

    @Test
    public void testContentLength() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.CONTENT_LENGTH, "1024");

        final HttpServerExchange exchange = mock(HttpServerExchange.class);
        when(exchange.getRequestMethod()).thenReturn(Methods.GET);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(exchange);
        assertEquals(1024, httpHeaders.getLength());
    }

    @Test
    public void testNullContentLength() throws Exception {
        final HeaderMap headerMap = new HeaderMap();

        final HttpServerExchange exchange = mock(HttpServerExchange.class);
        when(exchange.getRequestMethod()).thenReturn(Methods.GET);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(exchange);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    public void testInvalidContentLength() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.CONTENT_LENGTH, "x");

        final HttpServerExchange exchange = mock(HttpServerExchange.class);
        when(exchange.getRequestMethod()).thenReturn(Methods.GET);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(exchange);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    public void testCookies() throws Exception {
        final io.undertow.server.handlers.Cookie undertowCookie = new io.undertow.server.handlers.CookieImpl("k", "v");

        final Map<String, io.undertow.server.handlers.Cookie> undertowCookies = new HashMap<>();
        undertowCookies.put(undertowCookie.getName(), undertowCookie);

        final HeaderMap headerMap = new HeaderMap();

        final HttpServerExchange exchange = mock(HttpServerExchange.class);
        when(exchange.getRequestMethod()).thenReturn(Methods.GET);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);
        when(exchange.getRequestCookies()).thenReturn(undertowCookies);

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(exchange);
        final Map<String, Cookie> cookies = httpHeaders.getCookies();
        assertNotNull(cookies);
        assertEquals(1, cookies.size());
        assertEquals("v", cookies.get("k").getValue());
        assertEquals(cookies, httpHeaders.getCookies());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDate() {
        final HttpServerExchange exchange = mock(HttpServerExchange.class);
        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(exchange);
        httpHeaders.getDate();
    }
}
