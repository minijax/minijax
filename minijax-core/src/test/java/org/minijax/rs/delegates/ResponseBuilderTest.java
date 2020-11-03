package org.minijax.rs.delegates;

import static org.junit.jupiter.api.Assertions.*;

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

import org.junit.jupiter.api.Test;
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
        assertNotSame(rb1, rb2);
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

    @Test
    public void testEncoding() {
        assertThrows(UnsupportedOperationException.class, () -> {
        Response.ok().encoding(null);
    });
    }

    @Test
    public void testReplaceAll() {
        assertThrows(UnsupportedOperationException.class, () -> {
        Response.ok().replaceAll(null);
    });
    }

    @Test
    public void testLanguage() {
        assertThrows(UnsupportedOperationException.class, () -> {
        Response.ok().language((String) null);
    });
    }

    @Test
    public void testLocale() {
        assertThrows(UnsupportedOperationException.class, () -> {
        Response.ok().language((Locale) null);
    });
    }

    @Test
    public void testVariant() {
        assertThrows(UnsupportedOperationException.class, () -> {
        Response.ok().variant(null);
    });
    }

    @Test
    public void testContentLocation() {
        assertThrows(UnsupportedOperationException.class, () -> {
        Response.ok().contentLocation(null);
    });
    }

    @Test
    public void testExpires() {
        assertThrows(UnsupportedOperationException.class, () -> {
        Response.ok().expires(null);
    });
    }

    @Test
    public void testLastModified() {
        assertThrows(UnsupportedOperationException.class, () -> {
        Response.ok().lastModified(null);
    });
    }

    @Test
    public void testEntityTag() {
        assertThrows(UnsupportedOperationException.class, () -> {
        Response.ok().tag((EntityTag) null);
    });
    }

    @Test
    public void testTag() {
        assertThrows(UnsupportedOperationException.class, () -> {
        Response.ok().tag((String) null);
    });
    }

    @Test
    public void testVariants() {
        assertThrows(UnsupportedOperationException.class, () -> {
        Response.ok().variants((Variant[]) null);
    });
    }

    @Test
    public void testVariantsList() {
        assertThrows(UnsupportedOperationException.class, () -> {
        Response.ok().variants((List<Variant>) null);
    });
    }

    @Test
    public void testLinks() {
        assertThrows(UnsupportedOperationException.class, () -> {
        Response.ok().links((Link[]) null);
    });
    }

    @Test
    public void testLinkUri() {
        assertThrows(UnsupportedOperationException.class, () -> {
        Response.ok().link((URI) null, null);
    });
    }

    @Test
    public void testLink() {
        assertThrows(UnsupportedOperationException.class, () -> {
        Response.ok().link((String) null, null);
    });
    }
}
