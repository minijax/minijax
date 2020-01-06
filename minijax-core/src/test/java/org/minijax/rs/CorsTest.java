package org.minijax.rs;

import static org.junit.Assert.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.rs.test.MinijaxTest;

public class CorsTest extends MinijaxTest {

    @GET
    @Path("/api/test")
    public static String getApiTest() {
        return "ok";
    }

    @GET
    @Path("/home")
    public static String getHome() {
        return "ok";
    }

    @BeforeClass
    public static void setUpCorsTest() {
        resetServer();
        getServer().allowCors("/api/");
        register(CorsTest.class);
    }

    @Test
    public void testAllowCors() {
        final Response r = target("/api/test").request().header("Origin", "http://test").get();
        assertEquals("*", r.getHeaderString("Access-Control-Allow-Origin"));
    }

    @Test
    public void testNoOriginHeader() {
        final Response r = target("/api/test").request().get();
        assertNull(r.getHeaderString("Access-Control-Allow-Origin"));
    }

    @Test
    public void testDenyCors() {
        final Response r = target("/home").request().header("Origin", "http://test").get();
        assertNull(r.getHeaderString("Access-Control-Allow-Origin"));
    }
}
