package org.minijax.uri;

import static org.junit.Assert.*;

import javax.ws.rs.core.UriBuilder;

import org.junit.Test;

public class UriBuilderTest {

    @Test
    public void testDelegate() {
        assertTrue(UriBuilder.fromPath("/foo") instanceof MinijaxUriBuilder);
    }

    @Test
    public void testFromPath() {
        assertEquals("/foo", UriBuilder.fromPath("/foo").build().toString());
    }

    @Test
    public void testFromUri() {
        assertEquals("https://www.example.com/foo", UriBuilder.fromUri("https://www.example.com/foo").build().toString());
    }

    @Test
    public void testIpv6() {
        assertEquals("https://[::1]/foo", UriBuilder.fromUri("https://[::1]/foo").toTemplate());
    }

    @Test
    public void testSchemaTemplate() {
        assertEquals("{a}://www.example.com/foo", UriBuilder.fromUri("{a}://www.example.com/foo").toTemplate());
    }

    @Test
    public void testMissingSchema() {
        assertEquals("www.example.com/foo", UriBuilder.fromUri("www.example.com/foo").toTemplate());
    }

    @Test
    public void testMissingHost() {
        assertEquals("foo", UriBuilder.fromUri("foo").toTemplate());
    }

    @Test
    public void testUserInfo() {
        assertEquals("u:p@host/path", UriBuilder.fromUri("u:p@host/path").toTemplate());
    }

    @Test
    public void testHostPort() {
        assertEquals("www.example.com:8080/foo", UriBuilder.fromUri("www.example.com:8080/foo").toTemplate());
    }

    @Test
    public void testNoPathQuery() {
        assertEquals("www.example.com?test", UriBuilder.fromUri("www.example.com?test").toTemplate());
    }

    @Test
    public void testNoPathFragment() {
        assertEquals("www.example.com#test", UriBuilder.fromUri("www.example.com#test").toTemplate());
    }

    @Test
    public void testPathAndQuery() {
        assertEquals("www.example.com/foo?test", UriBuilder.fromUri("www.example.com/foo?test").toTemplate());
    }

    @Test
    public void testPathAndFragment() {
        assertEquals("www.example.com/foo#test", UriBuilder.fromUri("www.example.com/foo#test").toTemplate());
    }

    @Test
    public void testPathAndQueryAndFragment() {
        assertEquals("www.example.com/foo?bar#test", UriBuilder.fromUri("www.example.com/foo?bar#test").toTemplate());
    }

    @Test
    public void testSchemaBuild() {
        assertEquals("https://www.example.com", UriBuilder.fromUri("{a}://www.example.com").build("https").toString());
    }

    @Test
    public void testUriOverride() {
        assertEquals("https://foo.com/path", UriBuilder.fromUri("https://bar.com/path").uri("https://foo.com").build().toString());
    }

    @Test
    public void testSchemeOverride() {
        assertEquals("https://foo.com/path", UriBuilder.fromUri("http://foo.com/path").scheme("https").build().toString());
    }

    @Test
    public void testSchemeSpecificPartOverride() {
        assertEquals("https://foo.com/path", UriBuilder.fromUri("https://bar.com").schemeSpecificPart("foo.com/path").build().toString());
    }

    @Test
    public void testUserInfoOverride() {
        assertEquals("https://alice:pw@foo.com/path", UriBuilder.fromUri("https://foo.com/path").userInfo("alice:pw").build().toString());
    }

    @Test
    public void testHostOverride() {
        assertEquals("https://foo.com/path", UriBuilder.fromUri("https://bar.com/path").host("foo.com").build().toString());
    }

    @Test
    public void testPortOverride() {
        assertEquals("https://foo.com:8080/path", UriBuilder.fromUri("https://foo.com/path").port(8080).build().toString());
    }

    @Test
    public void testPathSlashes() {
        assertEquals("https://foo.com/a/b", UriBuilder.fromUri("https://foo.com").path("a").path("b").build().toString());
    }

    @Test
    public void testPathManualSlash() {
        assertEquals("https://foo.com/a/b", UriBuilder.fromUri("https://foo.com").path("a/").path("b").build().toString());
    }

    @Test
    public void testSegments() {
        assertEquals("https://foo.com/a/b", UriBuilder.fromUri("https://foo.com").segment("a", "b").build().toString());
    }

    @Test
    public void testQueryParam() {
        assertEquals("https://foo.com?a=b", UriBuilder.fromUri("https://foo.com").queryParam("a", "b").build().toString());
    }

    @Test
    public void testQueryParamAmpersand() {
        assertEquals("https://foo.com?a=b&c=d", UriBuilder.fromUri("https://foo.com").queryParam("a", "b").queryParam("c", "d").build().toString());
    }
}
