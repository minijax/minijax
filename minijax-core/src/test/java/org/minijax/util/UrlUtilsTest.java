package org.minijax.util;

import static org.junit.Assert.*;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;
import org.minijax.test.MockHttpServletRequest;

public class UrlUtilsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new UrlUtils();
    }

    @Test
    public void testConcatUrls() {
        assertEquals("/", UrlUtils.concatUrlPaths(null, null));
        assertEquals("/", UrlUtils.concatUrlPaths(null, ""));
        assertEquals("/", UrlUtils.concatUrlPaths("", null));
        assertEquals("/", UrlUtils.concatUrlPaths("", ""));
        assertEquals("/a/b", UrlUtils.concatUrlPaths("a", "b"));
        assertEquals("/a/b", UrlUtils.concatUrlPaths("/a", "/b"));
    }

    @Test
    public void testGetFullUrl() {
        assertEquals("http://www.example.com/", UrlUtils.getFullRequestUrl(makeRequest("http://www.example.com/")).toString());
        assertEquals("http://www.example.com/foo", UrlUtils.getFullRequestUrl(makeRequest("http://www.example.com/foo")).toString());
        assertEquals("http://www.example.com/foo?k=v", UrlUtils.getFullRequestUrl(makeRequest("http://www.example.com/foo?k=v")).toString());
    }

    @Test
    public void testForwardedProtocol() {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.add("X-Forwarded-Proto", "https");

        final MockHttpServletRequest req = new MockHttpServletRequest(null, URI.create("http://www.example.com/"), headers, null, null);
        assertEquals("https://www.example.com/", UrlUtils.getFullRequestUrl(req).toString());
    }

    private HttpServletRequest makeRequest(final String url) {
        return new MockHttpServletRequest("GET", URI.create(url));
    }
}
