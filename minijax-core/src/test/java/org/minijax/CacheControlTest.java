package org.minijax;

import static org.junit.Assert.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class CacheControlTest extends MinijaxTest {

    @GET
    @Path("/public")
    public static Response getPublic() {
        return Response.ok().cacheControl(CacheControl.valueOf("public")).build();
    }

    @GET
    @Path("/private")
    public static String getPrivate() {
        return "ok";
    }

    @Before
    public void setUp() {
        getServer().defaultCacheControl(CacheControl.valueOf("private"));
        register(CacheControlTest.class);
    }

    @Test
    public void testPublicCacheControl() {
        final Response r = target("/public").request().get();
        assertEquals("public", r.getHeaderString(HttpHeaders.CACHE_CONTROL));
    }

    @Test
    public void testPrivateCacheControl() {
        final Response r = target("/private").request().get();
        assertEquals("private", r.getHeaderString(HttpHeaders.CACHE_CONTROL));
    }
}
