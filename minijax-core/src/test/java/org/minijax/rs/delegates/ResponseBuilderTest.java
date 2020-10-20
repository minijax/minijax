package org.minijax.rs.delegates;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Variant;

import org.junit.Test;
import org.minijax.rs.test.MinijaxTest;

public class ResponseBuilderTest extends MinijaxTest {

    @Test
    public void testSetStatusCode() {
        final ResponseBuilder builder = Response.status(404);
        final Response response = builder.build();
        assertEquals(404, response.getStatus());
        assertEquals("Not Found", response.getStatusInfo().getReasonPhrase());
    }

    @Test
    public void testSetUnknownStatusCode() {
        final ResponseBuilder builder = Response.status(101);
        final Response response = builder.build();
        assertEquals(101, response.getStatus());
    }

    @Test
    public void testDelegate() {
        final ResponseBuilder builder = Response.ok();
        assertTrue(builder instanceof MinijaxResponseBuilder);
    }

    @Test
    public void testCacheControl() {
        final Response response = Response.ok().cacheControl(new CacheControl()).build();
        assertNotNull(response);
        assertNotNull(response.getHeaderString("Cache-Control"));
    }

    @Test
    public void testClone() {
        final ResponseBuilder rb1 = Response.ok();
        final ResponseBuilder rb2 = rb1.clone();
        assertFalse(rb1 == rb2);
    }

    @Test
    public void testNullCookie() {
        final Response response = Response.ok().build();
        assertNotNull(response);
        assertTrue(response.getCookies().isEmpty());
    }

    @Test
    public void testCookie() {
        final Response response = Response.ok().cookie(new NewCookie("a", "b")).build();
        assertNotNull(response);
        assertTrue(response.getCookies().containsKey("a"));
        assertEquals("b", response.getCookies().get("a").getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEncoding() {
        Response.ok().encoding(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReplaceAll() {
        Response.ok().replaceAll(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLanguage() {
        Response.ok().language((String) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLocale() {
        Response.ok().language((Locale) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVariant() {
        Response.ok().variant(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContentLocation() {
        Response.ok().contentLocation(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExpires() {
        Response.ok().expires(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLastModified() {
        Response.ok().lastModified(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntityTag() {
        Response.ok().tag((EntityTag) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTag() {
        Response.ok().tag((String) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVariants() {
        Response.ok().variants((Variant[]) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVariantsList() {
        Response.ok().variants((List<Variant>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLinks() {
        Response.ok().links((Link[]) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLinkUri() {
        Response.ok().link((URI) null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLink() {
        Response.ok().link((String) null, null);
    }
}
