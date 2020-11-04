package org.minijax.rs.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static java.util.Collections.*;

import java.nio.charset.StandardCharsets;

class UrlUtilsTest {

    @Test
    void testCtor() {
        assertThrows(UnsupportedOperationException.class, UrlUtils::new);
    }

    @Test
    void testConcatUrls() {
        assertEquals("/", UrlUtils.concatUrlPaths(null, null));
        assertEquals("/", UrlUtils.concatUrlPaths(null, ""));
        assertEquals("/", UrlUtils.concatUrlPaths("", null));
        assertEquals("/", UrlUtils.concatUrlPaths("", ""));
        assertEquals("/a/b", UrlUtils.concatUrlPaths("a", "b"));
        assertEquals("/a/b", UrlUtils.concatUrlPaths("/a", "/b"));
    }

    @Test
    void testUrlEncodeIgnoreTemplates() {
        assertEquals("foo", UrlUtils.urlEncode("foo", true, false));
        assertEquals("123", UrlUtils.urlEncode("123", true, false));
        assertEquals("a%20b", UrlUtils.urlEncode("a b", true, false));
        assertEquals("foo{bar}", UrlUtils.urlEncode("foo{bar}", true, false));
        assertEquals("foo{bar:[a-z]{1-3}}", UrlUtils.urlEncode("foo{bar:[a-z]{1-3}}", true, false));

        assertEquals("%f0%9f%98%81", UrlUtils.urlEncode(new String(
                new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x81 },
                StandardCharsets.UTF_8),
                true, false));
    }

    @Test
    void testUrlDecodeParams() {
        assertEquals(emptyMap(), UrlUtils.urlDecodeParams(null));
        assertEquals(emptyMap(), UrlUtils.urlDecodeParams(""));
        assertEquals(singletonMap("a", ""), UrlUtils.urlDecodeParams("a"));
    }
}
