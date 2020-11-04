package org.minijax.rs.uri;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Collections;

import jakarta.ws.rs.core.UriBuilder;

import org.junit.jupiter.api.Test;

class UriBuilderTest {

    @Test
    void testDelegate() {
        assertTrue(UriBuilder.fromPath("/foo") instanceof MinijaxUriBuilder);
    }

    @Test
    void testFromPath() {
        assertEquals("/foo", UriBuilder.fromPath("/foo").build().toString());
    }

    @Test
    void testFromUri() {
        assertEquals("https://www.example.com/foo",
                UriBuilder.fromUri("https://www.example.com/foo").build().toString());
    }

    @Test
    void testIpv6() {
        assertEquals("https://[::1]/foo", UriBuilder.fromUri("https://[::1]/foo").toTemplate());
    }

    @Test
    void testSchemaTemplate() {
        assertEquals("{a}://www.example.com/foo", UriBuilder.fromUri("{a}://www.example.com/foo").toTemplate());
    }

    @Test
    void testMissingSchema() {
        assertEquals("www.example.com/foo", UriBuilder.fromUri("www.example.com/foo").toTemplate());
    }

    @Test
    void testMissingHost() {
        assertEquals("foo", UriBuilder.fromUri("foo").toTemplate());
    }

    @Test
    void testUserInfo() {
        assertEquals("u:p@host/path", UriBuilder.fromUri("u:p@host/path").toTemplate());
    }

    @Test
    void testHostPort() {
        assertEquals("www.example.com:8080/foo", UriBuilder.fromUri("www.example.com:8080/foo").toTemplate());
    }

    @Test
    void testNoPathQuery() {
        assertEquals("www.example.com?test", UriBuilder.fromUri("www.example.com?test").toTemplate());
    }

    @Test
    void testNoPathFragment() {
        assertEquals("www.example.com#test", UriBuilder.fromUri("www.example.com#test").toTemplate());
    }

    @Test
    void testPathAndQuery() {
        assertEquals("www.example.com/foo?test", UriBuilder.fromUri("www.example.com/foo?test").toTemplate());
    }

    @Test
    void testPathAndFragment() {
        assertEquals("www.example.com/foo#test", UriBuilder.fromUri("www.example.com/foo#test").toTemplate());
    }

    @Test
    void testPathAndQueryAndFragment() {
        assertEquals("www.example.com/foo?bar#test", UriBuilder.fromUri("www.example.com/foo?bar#test").toTemplate());
    }

    @Test
    void testSchemaBuild() {
        assertEquals("https://www.example.com", UriBuilder.fromUri("{a}://www.example.com").build("https").toString());
    }

    @Test
    void testUriOverride() {
        assertEquals("https://foo.com/path",
                UriBuilder.fromUri("https://bar.com/path").uri("https://foo.com").build().toString());
    }

    @Test
    void testSchemeOverride() {
        assertEquals("https://foo.com/path",
                UriBuilder.fromUri("http://foo.com/path").scheme("https").build().toString());
    }

    @Test
    void testSchemeSpecificPartOverride() {
        assertEquals("https://foo.com/path",
                UriBuilder.fromUri("https://bar.com").schemeSpecificPart("foo.com/path").build().toString());
    }

    @Test
    void testUserInfoOverride() {
        assertEquals("https://alice:pw@foo.com/path",
                UriBuilder.fromUri("https://foo.com/path").userInfo("alice:pw").build().toString());
    }

    @Test
    void testHostOverride() {
        assertEquals("https://foo.com/path",
                UriBuilder.fromUri("https://bar.com/path").host("foo.com").build().toString());
    }

    @Test
    void testPortOverride() {
        assertEquals("https://foo.com:8080/path",
                UriBuilder.fromUri("https://foo.com/path").port(8080).build().toString());
    }

    @Test
    void testPathSlashes() {
        assertEquals("https://foo.com/a/b",
                UriBuilder.fromUri("https://foo.com").path("a").path("b").build().toString());
    }

    @Test
    void testPathManualSlash() {
        assertEquals("https://foo.com/a/b",
                UriBuilder.fromUri("https://foo.com").path("a/").path("b").build().toString());
    }

    @Test
    void testSegments() {
        assertEquals("https://foo.com/a/b", UriBuilder.fromUri("https://foo.com").segment("a", "b").build().toString());
    }

    @Test
    void testQueryParam() {
        assertEquals("https://foo.com?a=b",
                UriBuilder.fromUri("https://foo.com").queryParam("a", "b").build().toString());
    }

    @Test
    void testQueryParamAmpersand() {
        assertEquals("https://foo.com?a=b&c=d",
                UriBuilder.fromUri("https://foo.com").queryParam("a", "b").queryParam("c", "d").build().toString());
    }

    @Test
    void testReplaceFragment() {
        assertEquals("https://foo.com#foo", UriBuilder.fromUri("https://foo.com").fragment("foo").build().toString());
    }

    @Test
    void testReplaceQueryParam() {
        assertEquals("https://foo.com?c=d",
                UriBuilder.fromUri("https://foo.com?a=b").replaceQueryParam("c", "d").build().toString());
    }

    @Test
    void testClone() {
        final MinijaxUriBuilder b1 = (MinijaxUriBuilder) UriBuilder
                .fromUri("https://u:p@example.com:8443/path?a=b#fuzz");
        final MinijaxUriBuilder b2 = b1.clone();
        assertEquals(b1.build(), b2.build());
    }

    @Test
    void testFromUriEquivalence() {
        final String uriStr = "https://u:p@example.com:8443/path?a=b#fuzz";
        final URI uri = URI.create(uriStr);
        final MinijaxUriBuilder b1 = (MinijaxUriBuilder) UriBuilder.fromUri(uriStr);
        final MinijaxUriBuilder b2 = (MinijaxUriBuilder) UriBuilder.fromUri(uri);
        assertEquals(b1.build(), b2.build());
    }

    @Test
    void testTemplateWithColon() {
        assertEquals("https://example.com/foo",
                UriBuilder.fromUri("https://example.com/{name: .*}").build("foo").toString());
    }

    @Test
    void testTemplateWithNestedCurlies() {
        assertEquals("https://example.com/foo",
                UriBuilder.fromUri("https://example.com/{name: {}}").build("foo").toString());
    }

    @Test
    void testTrailingCurly() {
        assertThrows(IllegalArgumentException.class, () -> UriBuilder.fromUri("https://example.com/{name}}").build("foo"));

    }

    @Test
    void testMinimumPath() {
        assertEquals("https://www.example.com/", UriBuilder.fromUri("https://www.example.com/").build().toString());
    }

    @Test
    void testTrailingSlash() {
        assertEquals("https://www.example.com/foo/",
                UriBuilder.fromUri("https://www.example.com/foo/").build().toString());
    }

    /*
     * Unsupported operations
     */

    @Test
    void testPath1() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxUriBuilder().path(Object.class));
    }

    @Test
    void testPath2() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxUriBuilder().path(Object.class, "foo"));
    }

    @Test
    void testPath3() throws ReflectiveOperationException {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxUriBuilder().path(Object.class.getMethod("equals", Object.class)));
    }

    @Test
    void testReplaceMatrix() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxUriBuilder().replaceMatrix(null));
    }

    @Test
    void testMatrixParam() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxUriBuilder().matrixParam("foo", null, null));
    }

    @Test
    void testReplaceMatrixParam() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxUriBuilder().replaceMatrixParam("foo", null, null));
    }

    @Test
    void testResolveTemplateDefaultEncodeSlash() {
        assertEquals("https://example.com/p1%2fp2",
                UriBuilder.fromUri("https://example.com/{name}").resolveTemplate("name", "p1/p2").toTemplate());
    }

    @Test
    void testResolveTemplateDoNotEncodeSlash() {
        assertEquals("https://example.com/p1/p2",
                UriBuilder.fromUri("https://example.com/{name}").resolveTemplate("name", "p1/p2", false).toTemplate());
    }

    @Test
    void testResolveTemplateEncodeSlash() {
        assertEquals("https://example.com/p1%2fp2",
                UriBuilder.fromUri("https://example.com/{name}").resolveTemplate("name", "p1/p2", true).toTemplate());
    }

    @Test
    void testResolveTemplateFromEncoded() {
        assertEquals("https://example.com/%20", UriBuilder.fromUri("https://example.com/{name}")
                .resolveTemplateFromEncoded("name", "%20").toTemplate());
    }

    @Test
    void testResolveTemplatesDefaultEncodeSlash() {
        assertEquals("https://example.com/p1%2fp2", UriBuilder.fromUri("https://example.com/{name}")
                .resolveTemplates(Collections.singletonMap("name", "p1/p2")).toTemplate());
    }

    @Test
    void testResolveTemplatesDoNotEncodeSlash() {
        assertEquals("https://example.com/p1/p2", UriBuilder.fromUri("https://example.com/{name}")
                .resolveTemplates(Collections.singletonMap("name", "p1/p2"), false).toTemplate());
    }

    @Test
    void testResolveTemplatesEncodeSlash() {
        assertEquals("https://example.com/p1%2fp2", UriBuilder.fromUri("https://example.com/{name}")
                .resolveTemplates(Collections.singletonMap("name", "p1/p2"), true).toTemplate());
    }

    @Test
    void testResolveTemplatesFromEncoded() {
        assertEquals("https://example.com/%20", UriBuilder.fromUri("https://example.com/{name}")
                .resolveTemplatesFromEncoded(Collections.singletonMap("name", "%20")).toTemplate());
    }

    @Test
    void testBuildFromMap() {
        assertEquals("https://example.com/%20", UriBuilder.fromUri("https://example.com/{name}")
                .buildFromMap(Collections.singletonMap("name", " ")).toString());
    }

    @Test
    void testBuildFromEncodedMap() {
        assertEquals("https://example.com/%20", UriBuilder.fromUri("https://example.com/{name}")
                .buildFromEncodedMap(Collections.singletonMap("name", "%20")).toString());
    }

    @Test
    void testBuildEncodeSlashes() {
        assertEquals("https://example.com/p1%2fp2",
                UriBuilder.fromUri("https://example.com/{name}").build(new Object[] { "p1/p2" }, true).toString());
    }

    @Test
    void testBuildDoNotEncodeSlashes() {
        assertEquals("https://example.com/p1/p2",
                UriBuilder.fromUri("https://example.com/{name}").build(new Object[] { "p1/p2" }, false).toString());
    }

    @Test
    void testBuildFromEncoded() {
        assertEquals("https://example.com/%20",
                UriBuilder.fromUri("https://example.com/{name}").buildFromEncoded("%20").toString());
    }
}
