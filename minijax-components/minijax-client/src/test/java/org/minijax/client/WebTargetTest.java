package org.minijax.client;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WebTargetTest {
    private MinijaxClient client;
    private MinijaxClientWebTarget rootTarget;

    @BeforeEach
    public void setUp() {
        client = new MinijaxClient();
        rootTarget = target("https://example.com");
    }

    public MinijaxClientWebTarget target(final String uri) {
        return client.target(uri);
    }

    @Test
    void testCtor() {
        try (final MinijaxClient client = new MinijaxClient()) {
            final MinijaxClientWebTarget target = client.target("https://example.com");
            assertEquals(client, target.getClient());
            assertNotNull(target.getUriBuilder());
            assertEquals("https://example.com", target.getUri().toString());
        }
    }

    @Test
    void testPath() {
        assertEquals("https://example.com/bar", rootTarget.path("/bar").getUri().toString());
    }

    @Test
    void testPathSlashes() {
        assertEquals("https://example.com/a/b", rootTarget.path("a").path("b").getUri().toString());
    }

    @Test
    void testPathManualSlash() {
        assertEquals("https://example.com/a/b", rootTarget.path("a/").path("b").getUri().toString());
    }

    @Test
    void testQueryParam() {
        assertEquals("https://example.com?a=b", rootTarget.queryParam("a", "b").getUri().toString());
    }

    @Test
    void testQueryParamAmpersand() {
        assertEquals("https://example.com?a=b&c=d",
                rootTarget.queryParam("a", "b").queryParam("c", "d").getUri().toString());
    }

    @Test
    void testResolveTemplate() {
        assertEquals("https://example.com/bar",
                target("https://example.com/{x}").resolveTemplate("x", "bar").getUri().toString());
    }

    @Test
    void testResolveTemplateIgnoreSlashes() {
        assertEquals("https://example.com/p1/p2",
                target("https://example.com/{x}").resolveTemplate("x", "p1/p2", false).getUri().toString());
    }

    @Test
    void testResolveTemplateFromEncoded() {
        assertEquals("https://example.com/%20",
                target("https://example.com/{x}").resolveTemplateFromEncoded("x", "%20").getUri().toString());
    }

    @Test
    void testResolveTemplates() {
        final Map<String, Object> map = new HashMap<>();
        map.put("x", "bar");
        map.put("y", "baz");
        assertEquals("https://example.com/bar/baz",
                target("https://example.com/{x}/{y}").resolveTemplates(map).getUri().toString());
    }

    @Test
    void testResolveTemplatesIgnoreSlashes() {
        final Map<String, Object> map = new HashMap<>();
        map.put("x", "bar");
        map.put("y", "p1/p2");
        assertEquals("https://example.com/bar/p1/p2",
                target("https://example.com/{x}/{y}").resolveTemplates(map, false).getUri().toString());
    }

    @Test
    void testResolveTemplatesFromEncoded() {
        final Map<String, Object> map = new HashMap<>();
        map.put("x", "bar");
        map.put("y", "%20");
        assertEquals("https://example.com/bar/%20",
                target("https://example.com/{x}/{y}").resolveTemplatesFromEncoded(map).getUri().toString());
    }

    @Test
    void testGetConfiguration() {
        assertThrows(UnsupportedOperationException.class, () -> rootTarget.getConfiguration());
    }

    @Test
    void testProperty() {
        assertThrows(UnsupportedOperationException.class, () -> rootTarget.property("name", "value"));
    }

    @Test
    void testRegister1() {
        assertThrows(UnsupportedOperationException.class, () -> rootTarget.register(Object.class));
    }

    @Test
    void testRegister2() {
        assertThrows(UnsupportedOperationException.class, () -> rootTarget.register(Object.class, 0));
    }

    @Test
    void testRegister3() {
        assertThrows(UnsupportedOperationException.class, () -> rootTarget.register(Object.class, Object.class));
    }

    @Test
    void testRegister4() {
        assertThrows(UnsupportedOperationException.class, () -> rootTarget.register(Object.class, Collections.emptyMap()));
    }

    @Test
    void testRegister5() {
        assertThrows(UnsupportedOperationException.class, () -> rootTarget.register(new Object()));
    }

    @Test
    void testRegister6() {
        assertThrows(UnsupportedOperationException.class, () -> rootTarget.register(new Object(), 0));
    }

    @Test
    void testRegister7() {
        assertThrows(UnsupportedOperationException.class, () -> rootTarget.register(new Object(), Object.class));
    }

    @Test
    void testRegister8() {
        assertThrows(UnsupportedOperationException.class, () -> rootTarget.register(new Object(), Collections.emptyMap()));
    }
}
