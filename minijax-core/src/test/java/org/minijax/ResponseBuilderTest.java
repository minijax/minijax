package org.minijax;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Variant;

import org.junit.Test;

public class ResponseBuilderTest {

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
