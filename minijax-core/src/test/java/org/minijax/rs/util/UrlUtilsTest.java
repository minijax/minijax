package org.minijax.rs.util;

import static java.util.Collections.*;
import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

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
    public void testUrlEncodeIgnoreTemplates() {
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
    public void testUrlDecodeParams() {
        assertEquals(emptyMap(), UrlUtils.urlDecodeParams(null));
        assertEquals(emptyMap(), UrlUtils.urlDecodeParams(""));
        assertEquals(singletonMap("a", ""), UrlUtils.urlDecodeParams("a"));
    }
}
