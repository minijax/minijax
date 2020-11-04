package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;
import org.minijax.rs.util.CacheControlUtils;

class CacheControlTest extends MinijaxTest {

    @GET
    @Path("/public")
    public static Response getPublic() {
        return Response.ok().cacheControl(CacheControlUtils.fromString("public")).build();
    }

    @GET
    @Path("/private")
    public static String getPrivate() {
        return "ok";
    }

    @BeforeAll
    public static void setUpCacheControlTest() {
        resetServer();
        getServer().defaultCacheControl(CacheControlUtils.fromString("private"));
        register(CacheControlTest.class);
    }

    @Test
    void testPublicCacheControl() {
        final Response r = target("/public").request().get();
        assertEquals("public", r.getHeaderString(HttpHeaders.CACHE_CONTROL));
    }

    @Test
    void testPrivateCacheControl() {
        final Response r = target("/private").request().get();
        assertEquals("private", r.getHeaderString(HttpHeaders.CACHE_CONTROL));
    }
}
