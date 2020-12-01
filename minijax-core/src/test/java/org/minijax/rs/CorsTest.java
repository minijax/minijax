package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

class CorsTest extends MinijaxTest {

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

    @GET
    @Path("/bad")
    public static String getBadRequest() {
        throw new BadRequestException("bad");
    }

    @BeforeAll
    public static void setUpCorsTest() {
        resetServer();
        getServer().allowCors("/api/");
        register(CorsTest.class);
    }

    @Test
    void testAllowCors() {
        final Response r = target("/api/test").request().header("Origin", "http://test").get();
        assertEquals("http://test", r.getHeaderString("Access-Control-Allow-Origin"));
    }

    @Test
    void testOptionsCors() {
        final Response r = target("/api/test").request().header("Origin", "http://test").options();
        assertEquals("http://test", r.getHeaderString("Access-Control-Allow-Origin"));

        final MultivaluedMap<String, Object> headers = r.getHeaders();
        assertEquals(1, headers.get("Access-Control-Allow-Origin").size());
        assertEquals("http://test", headers.get("Access-Control-Allow-Origin").get(0));
    }

    @Test
    void testNoOriginHeader() {
        final Response r = target("/api/test").request().get();
        assertNull(r.getHeaderString("Access-Control-Allow-Origin"));
    }

    @Test
    void testDenyCors() {
        final Response r = target("/home").request().header("Origin", "http://test").get();
        assertNull(r.getHeaderString("Access-Control-Allow-Origin"));
    }

    @Test
    void testBadRequestCors() {
        final Response r = target("/bad").request().header("Origin", "http://test").get();
        assertNull(r.getHeaderString("Access-Control-Allow-Origin"));
    }

    @Test
    void testRequestHeaders() {
        final Response r = target("/api/test").request().header("Origin", "http://test").header("Access-Control-Request-Headers", "foo").get();
        assertEquals("http://test", r.getHeaderString("Access-Control-Allow-Origin"));
        assertEquals("foo", r.getHeaderString("Access-Control-Allow-Headers"));
    }
}
