package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.ResourceContext;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Providers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

class ContextParamTest extends MinijaxTest {

    // 9.2.1
    @GET
    @Path("/application")
    public static String getApplication(@Context final Application application) {
        return "ok";
    }

    // 9.2.2
    @GET
    @Path("/uriinfo")
    public static String getUriInfo(@Context final UriInfo uriInfo) {
        return "ok";
    }

    // 9.2.3
    @GET
    @Path("/httpheaders")
    public static String getHttpHeaders(@Context final HttpHeaders httpHeaders) {
        return "ok";
    }

    // 9.2.4
    @GET
    @Path("/request")
    public static String getRequest(@Context final Request request) {
        return "ok";
    }

    // 9.2.5
    @GET
    @Path("/securitycontext")
    public static String getSecurityContext(@Context final SecurityContext securityContext) {
        return "ok";
    }

    // 9.2.6
    @GET
    @Path("/providers")
    public static String getProviders(@Context final Providers providers) {
        return "ok";
    }

    // 9.2.7
    @GET
    @Path("/resourcecontext")
    public static String getResourceContext(@Context final ResourceContext resourceContext) {
        return "ok";
    }

    // 9.2.8
    @GET
    @Path("/configuration")
    public static String getConfiguration(@Context final Configuration configuration) {
        return "ok";
    }

    @GET
    @Path("/unknown")
    public static String getUnknown(@Context final Object obj) {
        return null;
    }

    @BeforeAll
    public static void setUpContextParamTest() {
        resetServer();
        register(ContextParamTest.class);
    }

    // 9.2.1
    @Test
    void testApplication() {
        assertEquals("ok", target("/application").request().get(String.class));
    }

    // 9.2.2
    @Test
    void testUriInfo() {
        assertEquals("ok", target("/uriinfo").request().get(String.class));
    }

    // 9.2.3
    @Test
    void testHttpHeaders() {
        assertEquals("ok", target("/httpheaders").request().get(String.class));
    }

    // 9.2.4
    @Test
    void testRequest() {
        assertEquals("ok", target("/request").request().get(String.class));
    }

    // 9.2.5
    @Test
    void testSecurityContext() {
        assertEquals("ok", target("/securitycontext").request().get(String.class));
    }

    // 9.2.6
    @Test
    void testProviders() {
        assertEquals("ok", target("/providers").request().get(String.class));
    }

    // 9.2.7
    @Test
    void testResourceContext() {
        assertEquals(200, target("/resourcecontext").request().get().getStatus());
        assertEquals("ok", target("/resourcecontext").request().get(String.class));
    }

    // 9.2.8
    @Test
    void testConfiguration() {
        assertEquals("ok", target("/configuration").request().get(String.class));
    }

    @Test
    void testUnknown() {
        assertEquals(500, target("/unknown").request().get().getStatus());
    }
}
