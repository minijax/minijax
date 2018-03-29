package org.minijax.uri;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.Collections;

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

    @Test
    public void testReplaceFragment() {
        assertEquals("https://foo.com#foo", UriBuilder.fromUri("https://foo.com").fragment("foo").build().toString());
    }

    @Test
    public void testReplaceQueryParam() {
        assertEquals("https://foo.com?c=d", UriBuilder.fromUri("https://foo.com?a=b").replaceQueryParam("c", "d").build().toString());
    }

    @Test
    public void testClone() {
        final MinijaxUriBuilder b1 = (MinijaxUriBuilder) UriBuilder.fromUri("https://u:p@example.com:8443/path?a=b#fuzz");
        final MinijaxUriBuilder b2 = b1.clone();
        assertEquals(b1.build(), b2.build());
    }

    @Test
    public void testFromUriEquivalence() {
        final String uriStr = "https://u:p@example.com:8443/path?a=b#fuzz";
        final URI uri = URI.create(uriStr);
        final MinijaxUriBuilder b1 = (MinijaxUriBuilder) UriBuilder.fromUri(uriStr);
        final MinijaxUriBuilder b2 = (MinijaxUriBuilder) UriBuilder.fromUri(uri);
        assertEquals(b1.build(), b2.build());
    }

    @Test
    public void testTemplateWithColon() {
        assertEquals("https://example.com/foo", UriBuilder.fromUri("https://example.com/{name: .*}").build("foo").toString());
    }

    @Test
    public void testTemplateWithNestedCurlies() {
        assertEquals("https://example.com/foo", UriBuilder.fromUri("https://example.com/{name: {}}").build("foo").toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTrailingCurly() {
        UriBuilder.fromUri("https://example.com/{name}}").build("foo");
    }

    /*
     * Unsupported operations
     */

    @Test(expected = UnsupportedOperationException.class)
    public void testPath1() {
        new MinijaxUriBuilder().path(Object.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPath2() {
        new MinijaxUriBuilder().path(Object.class, "foo");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPath3() throws ReflectiveOperationException {
        new MinijaxUriBuilder().path(Object.class.getMethod("equals", Object.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReplaceMatrix() {
        new MinijaxUriBuilder().replaceMatrix(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMatrixParam() {
        new MinijaxUriBuilder().matrixParam("foo", null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReplaceMatrixParam() {
        new MinijaxUriBuilder().replaceMatrixParam("foo", null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResolveTemplate1() {
        new MinijaxUriBuilder().resolveTemplate("foo", null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResolveTemplate2() {
        new MinijaxUriBuilder().resolveTemplate("foo", null, false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResolveTemplateFromEncoded() {
        new MinijaxUriBuilder().resolveTemplateFromEncoded("foo", null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResolveTemplates1() {
        new MinijaxUriBuilder().resolveTemplates(Collections.emptyMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResolveTemplates2() {
        new MinijaxUriBuilder().resolveTemplates(Collections.emptyMap(), false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResolveTemplatesFromEncoded() {
        new MinijaxUriBuilder().resolveTemplatesFromEncoded(Collections.emptyMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBuildFromMap() {
        new MinijaxUriBuilder().buildFromMap(Collections.emptyMap(), false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBuildFromEncodedMap() {
        new MinijaxUriBuilder().buildFromEncodedMap(Collections.emptyMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBuild() {
        new MinijaxUriBuilder().build(new Object[0], false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBuildFromEncoded() {
        new MinijaxUriBuilder().buildFromEncoded(new Object[0]);
    }
}
