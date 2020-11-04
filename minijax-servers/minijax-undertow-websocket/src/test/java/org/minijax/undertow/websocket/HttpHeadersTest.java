package org.minijax.undertow.websocket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;

import org.junit.jupiter.api.Test;

import io.undertow.websockets.spi.WebSocketHttpExchange;

class HttpHeadersTest {

    @Test
    void testBasic() throws Exception {
        final Map<String, List<String>> headerMap = new HashMap<>();
        headerMap.computeIfAbsent(HttpHeaders.CONTENT_TYPE, k -> new ArrayList<>()).add("text/plain");

        final WebSocketHttpExchange exchange = mock(WebSocketHttpExchange.class);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowWebSocketHttpHeaders httpHeaders = new MinijaxUndertowWebSocketHttpHeaders(exchange);
        assertEquals("text/plain", httpHeaders.getHeaderString("Content-Type"));
    }

    @Test
    void testMultiple() throws Exception {
        final Map<String, List<String>> headerMap = new HashMap<>();
        headerMap.computeIfAbsent("X-Foo", k -> new ArrayList<>()).add("bar");
        headerMap.computeIfAbsent("X-Foo", k -> new ArrayList<>()).add("baz");

        final WebSocketHttpExchange exchange = mock(WebSocketHttpExchange.class);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowWebSocketHttpHeaders httpHeaders = new MinijaxUndertowWebSocketHttpHeaders(exchange);
        assertEquals(Arrays.asList("bar", "baz"), httpHeaders.getRequestHeader("X-Foo"));

        final MultivaluedMap<String, String> multiMap = httpHeaders.getRequestHeaders();
        assertNotNull(multiMap);
        assertEquals(multiMap, httpHeaders.getRequestHeaders());
    }

    @Test
    void testAccept() throws Exception {
        final Map<String, List<String>> headerMap = new HashMap<>();
        headerMap.computeIfAbsent(HttpHeaders.ACCEPT, k -> new ArrayList<>()).add("text/plain");

        final WebSocketHttpExchange exchange = mock(WebSocketHttpExchange.class);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowWebSocketHttpHeaders httpHeaders = new MinijaxUndertowWebSocketHttpHeaders(exchange);
        assertEquals(Collections.singletonList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
        assertEquals(Collections.singletonList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
    }

    @Test
    void testAcceptLanguage() throws Exception {
        final Map<String, List<String>> headerMap = new HashMap<>();
        headerMap.computeIfAbsent(HttpHeaders.ACCEPT_LANGUAGE, k -> new ArrayList<>()).add("en-US");

        final WebSocketHttpExchange exchange = mock(WebSocketHttpExchange.class);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowWebSocketHttpHeaders httpHeaders = new MinijaxUndertowWebSocketHttpHeaders(exchange);
        assertEquals(Collections.singletonList(Locale.US), httpHeaders.getAcceptableLanguages());
        assertEquals(Collections.singletonList(Locale.US), httpHeaders.getAcceptableLanguages());
    }

    @Test
    void testMediaType() throws Exception {
        final Map<String, List<String>> headerMap = new HashMap<>();
        headerMap.computeIfAbsent(HttpHeaders.CONTENT_TYPE, k -> new ArrayList<>()).add("text/plain");

        final WebSocketHttpExchange exchange = mock(WebSocketHttpExchange.class);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowWebSocketHttpHeaders httpHeaders = new MinijaxUndertowWebSocketHttpHeaders(exchange);
        assertEquals(MediaType.TEXT_PLAIN_TYPE, httpHeaders.getMediaType());
    }

    @Test
    void testNullMediaType() throws Exception {
        final Map<String, List<String>> headerMap = new HashMap<>();

        final WebSocketHttpExchange exchange = mock(WebSocketHttpExchange.class);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowWebSocketHttpHeaders httpHeaders = new MinijaxUndertowWebSocketHttpHeaders(exchange);
        assertNull(httpHeaders.getMediaType());
    }

    @Test
    void testLanguage() throws Exception {
        final Map<String, List<String>> headerMap = new HashMap<>();
        headerMap.computeIfAbsent(HttpHeaders.CONTENT_LANGUAGE, k -> new ArrayList<>()).add("en-US");

        final WebSocketHttpExchange exchange = mock(WebSocketHttpExchange.class);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowWebSocketHttpHeaders httpHeaders = new MinijaxUndertowWebSocketHttpHeaders(exchange);
        assertEquals(Locale.US, httpHeaders.getLanguage());
    }

    @Test
    void testNullLanguage() throws Exception {
        final Map<String, List<String>> headerMap = new HashMap<>();

        final WebSocketHttpExchange exchange = mock(WebSocketHttpExchange.class);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowWebSocketHttpHeaders httpHeaders = new MinijaxUndertowWebSocketHttpHeaders(exchange);
        assertNull(httpHeaders.getLanguage());
    }

    @Test
    void testContentLength() throws Exception {
        final Map<String, List<String>> headerMap = new HashMap<>();
        headerMap.computeIfAbsent(HttpHeaders.CONTENT_LENGTH, k -> new ArrayList<>()).add("1024");

        final WebSocketHttpExchange exchange = mock(WebSocketHttpExchange.class);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowWebSocketHttpHeaders httpHeaders = new MinijaxUndertowWebSocketHttpHeaders(exchange);
        assertEquals(1024, httpHeaders.getLength());
    }

    @Test
    void testNullContentLength() throws Exception {
        final Map<String, List<String>> headerMap = new HashMap<>();

        final WebSocketHttpExchange exchange = mock(WebSocketHttpExchange.class);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowWebSocketHttpHeaders httpHeaders = new MinijaxUndertowWebSocketHttpHeaders(exchange);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    void testInvalidContentLength() throws Exception {
        final Map<String, List<String>> headerMap = new HashMap<>();
        headerMap.computeIfAbsent(HttpHeaders.CONTENT_LENGTH, k -> new ArrayList<>()).add("x");

        final WebSocketHttpExchange exchange = mock(WebSocketHttpExchange.class);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowWebSocketHttpHeaders httpHeaders = new MinijaxUndertowWebSocketHttpHeaders(exchange);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    void testCookies() throws Exception {
        final Map<String, List<String>> headerMap = new HashMap<>();
        headerMap.computeIfAbsent(HttpHeaders.COOKIE, k -> new ArrayList<>()).add("k=v");

        final WebSocketHttpExchange exchange = mock(WebSocketHttpExchange.class);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowWebSocketHttpHeaders httpHeaders = new MinijaxUndertowWebSocketHttpHeaders(exchange);
        final Map<String, Cookie> cookies = httpHeaders.getCookies();
        assertNotNull(cookies);
        assertEquals(1, cookies.size());
        assertEquals("v", cookies.get("k").getValue());
        assertEquals(cookies, httpHeaders.getCookies());
    }

    @Test
    void testCompoundCookies() throws Exception {
        final Map<String, List<String>> headerMap = new HashMap<>();
        headerMap.computeIfAbsent(HttpHeaders.COOKIE, k -> new ArrayList<>()).add("k=v; a=b;c=d");
        headerMap.computeIfAbsent(HttpHeaders.COOKIE, k -> new ArrayList<>()).add("e=f");

        final WebSocketHttpExchange exchange = mock(WebSocketHttpExchange.class);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        final MinijaxUndertowWebSocketHttpHeaders httpHeaders = new MinijaxUndertowWebSocketHttpHeaders(exchange);
        final Map<String, Cookie> cookies = httpHeaders.getCookies();
        assertNotNull(cookies);
        assertEquals(4, cookies.size());
        assertEquals("v", cookies.get("k").getValue());
        assertEquals("b", cookies.get("a").getValue());
        assertEquals("d", cookies.get("c").getValue());
        assertEquals("f", cookies.get("e").getValue());
        assertEquals(cookies, httpHeaders.getCookies());
    }

    @Test
    void testDate() {
        assertThrows(UnsupportedOperationException.class, () -> {
        final WebSocketHttpExchange exchange = mock(WebSocketHttpExchange.class);
        final MinijaxUndertowWebSocketHttpHeaders httpHeaders = new MinijaxUndertowWebSocketHttpHeaders(exchange);
        httpHeaders.getDate();
    });
    }
}
