package org.minijax.undertow;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

public class HttpHeadersTest {

    @Test
    public void testBasic() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.CONTENT_TYPE, "text/plain");

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertEquals("text/plain", httpHeaders.getHeaderString("Content-Type"));
    }

    @Test
    public void testMultiple() throws Exception {
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
    public void testAccept() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.ACCEPT, "text/plain");

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertEquals(Arrays.asList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
        assertEquals(Arrays.asList(MediaType.TEXT_PLAIN_TYPE), httpHeaders.getAcceptableMediaTypes());
    }

    @Test
    public void testAcceptLanguage() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.ACCEPT_LANGUAGE, "en-US");

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertEquals(Arrays.asList(Locale.US), httpHeaders.getAcceptableLanguages());
        assertEquals(Arrays.asList(Locale.US), httpHeaders.getAcceptableLanguages());
    }

    @Test
    public void testMediaType() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.CONTENT_TYPE, "text/plain");

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertEquals(MediaType.TEXT_PLAIN_TYPE, httpHeaders.getMediaType());
    }

    @Test
    public void testNullMediaType() throws Exception {
        final HeaderMap headerMap = new HeaderMap();

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertNull(httpHeaders.getMediaType());
    }

    @Test
    public void testLanguage() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.CONTENT_LANGUAGE, "en-US");

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertEquals(Locale.US, httpHeaders.getLanguage());
    }

    @Test
    public void testNullLanguage() throws Exception {
        final HeaderMap headerMap = new HeaderMap();

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertNull(httpHeaders.getLanguage());
    }

    @Test
    public void testContentLength() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.CONTENT_LENGTH, "1024");

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertEquals(1024, httpHeaders.getLength());
    }

    @Test
    public void testNullContentLength() throws Exception {
        final HeaderMap headerMap = new HeaderMap();

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    public void testInvalidContentLength() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.CONTENT_LENGTH, "x");

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        assertEquals(-1, httpHeaders.getLength());
    }

    @Test
    public void testCookies() throws Exception {
        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.COOKIE, "k=v");

        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        final Map<String, Cookie> cookies = httpHeaders.getCookies();
        assertNotNull(cookies);
        assertEquals(1, cookies.size());
        assertEquals("v", cookies.get("k").getValue());
        assertEquals(cookies, httpHeaders.getCookies());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDate() {
        final HeaderMap headerMap = new HeaderMap();
        final MinijaxUndertowHttpHeaders httpHeaders = new MinijaxUndertowHttpHeaders(headerMap);
        httpHeaders.getDate();
    }
}
