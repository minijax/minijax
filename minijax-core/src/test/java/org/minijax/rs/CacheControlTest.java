package org.minijax.rs;

import static org.junit.Assert.*;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.rs.test.MinijaxTest;

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

    @BeforeClass
    public static void setUpCacheControlTest() {
        resetServer();
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
