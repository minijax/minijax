package org.minijax.client;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class WebTargetTest {
    private MinijaxClient client;

    @Before
    public void setUp() {
        client = new MinijaxClient();
    }

    public MinijaxClientWebTarget target(final String uri) {
        return client.target(uri);
    }

    @Test
    public void testCtor() {
        try (final MinijaxClient client = new MinijaxClient()) {
            final MinijaxClientWebTarget target = client.target("http://foo.com");
            assertEquals(client, target.getClient());
            assertNotNull(target.getUriBuilder());
            assertEquals("http://foo.com", target.getUri().toString());
        }
    }

    @Test
    public void testPath() {
        assertEquals("http://foo.com/bar", target("http://foo.com").path("/bar").getUri().toString());
    }

    @Test
    public void testPathSlashes() {
        assertEquals("https://foo.com/a/b", target("https://foo.com").path("a").path("b").getUri().toString());
    }

    @Test
    public void testPathManualSlash() {
        assertEquals("https://foo.com/a/b", target("https://foo.com").path("a/").path("b").getUri().toString());
    }

    @Test
    public void testQueryParam() {
        assertEquals("https://foo.com?a=b", target("https://foo.com").queryParam("a", "b").getUri().toString());
    }

    @Test
    public void testQueryParamAmpersand() {
        assertEquals("https://foo.com?a=b&c=d", target("https://foo.com").queryParam("a", "b").queryParam("c", "d").getUri().toString());
    }

    @Test
    public void testResolveTemplate() {
        assertEquals("http://foo.com/bar", target("http://foo.com/{x}").resolveTemplate("x", "bar").getUri().toString());
    }

    @Test
    public void testResolveTemplateIgnoreSlashes() {
        assertEquals("http://foo.com/p1/p2", target("http://foo.com/{x}").resolveTemplate("x", "p1/p2", false).getUri().toString());
    }

    @Test
    public void testResolveTemplateFromEncoded() {
        assertEquals("http://foo.com/%20", target("http://foo.com/{x}").resolveTemplateFromEncoded("x", "%20").getUri().toString());
    }

    @Test
    public void testResolveTemplates() {
        final Map<String, Object> map = new HashMap<>();
        map.put("x", "bar");
        map.put("y", "baz");
        assertEquals("http://foo.com/bar/baz", target("http://foo.com/{x}/{y}").resolveTemplates(map).getUri().toString());
    }

    @Test
    public void testResolveTemplatesIgnoreSlashes() {
        final Map<String, Object> map = new HashMap<>();
        map.put("x", "bar");
        map.put("y", "p1/p2");
        assertEquals("http://foo.com/bar/p1/p2", target("http://foo.com/{x}/{y}").resolveTemplates(map, false).getUri().toString());
    }

    @Test
    public void testResolveTemplatesFromEncoded() {
        final Map<String, Object> map = new HashMap<>();
        map.put("x", "bar");
        map.put("y", "%20");
        assertEquals("http://foo.com/bar/%20", target("http://foo.com/{x}/{y}").resolveTemplatesFromEncoded(map).getUri().toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetConfiguration() {
        target("/").getConfiguration();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testProperty() {
        target("/").property("name", "value");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister1() {
        target("/").register(Object.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister2() {
        target("/").register(Object.class, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister3() {
        target("/").register(Object.class, Object.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister4() {
        target("/").register(Object.class, Collections.emptyMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister5() {
        target("/").register(new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister6() {
        target("/").register(new Object(), 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister7() {
        target("/").register(new Object(), Object.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister8() {
        target("/").register(new Object(), Collections.emptyMap());
    }
}
