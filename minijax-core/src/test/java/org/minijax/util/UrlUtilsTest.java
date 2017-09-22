package org.minijax.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.minijax.servlet.MockHttpServletRequest;

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
    public void testGetPathParams() {
        assertTrue(UrlUtils.getPathParams(null).isEmpty());
        assertTrue(UrlUtils.getPathParams("").isEmpty());
        assertTrue(UrlUtils.getPathParams("/").isEmpty());
        assertTrue(UrlUtils.getPathParams("/foo").isEmpty());
        assertEquals("name", UrlUtils.getPathParams("/foo/{name}").get(0));
        assertEquals("name", UrlUtils.getPathParams("/foo/{name:[A-Z]+}").get(0));
        assertEquals("id", UrlUtils.getPathParams("/foo/{name}/bar/{id}").get(1));
    }

    @Test
    public void testConvertPathToRegex() {
        assertEquals("/", UrlUtils.convertPathToRegex("/"));
        assertEquals("/x", UrlUtils.convertPathToRegex("/x"));
        assertEquals("/x/y", UrlUtils.convertPathToRegex("/x/y"));
        assertEquals("/x/(?<id>[^/]+)", UrlUtils.convertPathToRegex("/x/{id}"));
        assertEquals("/x/(?<id>[0-9]+)", UrlUtils.convertPathToRegex("/x/{id:[0-9]+}"));
        assertEquals("/x/(?<id>\\d+)", UrlUtils.convertPathToRegex("/x/{id:\\d+}"));
    }

    @Test
    public void testGetFullUrl() {
        assertEquals("http://www.example.com/", UrlUtils.getFullRequestUrl(makeRequest("http://www.example.com/")).toString());
        assertEquals("http://www.example.com/foo", UrlUtils.getFullRequestUrl(makeRequest("http://www.example.com/foo")).toString());
        assertEquals("http://www.example.com/foo?k=v", UrlUtils.getFullRequestUrl(makeRequest("http://www.example.com/foo?k=v")).toString());
    }

    @Test
    public void testForwardedProtocol() {
        final Map<String, String> headers = new HashMap<>();
        headers.put("X-Forwarded-Proto", "https");

        final MockHttpServletRequest req = new MockHttpServletRequest(headers, null, null, "http://www.example.com/", null);
        assertEquals("https://www.example.com/", UrlUtils.getFullRequestUrl(req).toString());
    }

    private HttpServletRequest makeRequest(final String url) {
        return new MockHttpServletRequest(null, null, null, url, null);
    }
}
