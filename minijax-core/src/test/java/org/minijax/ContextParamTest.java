package org.minijax;

import static org.junit.Assert.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class ContextParamTest extends MinijaxTest {

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

    // 10.1
    @GET
    @Path("/servletconfig")
    public static String getHttpServletConfig(@Context final ServletConfig servletConfig) {
        return "ok";
    }

    @GET
    @Path("/servletcontext")
    public static String getHttpServletContext(@Context final ServletContext servletContext) {
        return "ok";
    }

    @GET
    @Path("/httpservletrequest")
    public static String getHttpServletRequest(@Context final HttpServletRequest request) {
        return "ok";
    }

    @GET
    @Path("/httpservletresponse")
    public static String getHttpServletResponse(@Context final HttpServletResponse response) {
        return "ok";
    }

    @GET
    @Path("/unknown")
    public static String getUnknown(@Context final Object obj) {
        return null;
    }

    @BeforeClass
    public static void setUpContextParamTest() {
        resetServer();
        register(ContextParamTest.class);
    }

    // 9.2.1
    @Test
    public void testApplication() {
        assertEquals("ok", target("/application").request().get(String.class));
    }

    // 9.2.2
    @Test
    public void testUriInfo() {
        assertEquals("ok", target("/uriinfo").request().get(String.class));
    }

    // 9.2.3
    @Test
    public void testHttpHeaders() {
        assertEquals("ok", target("/httpheaders").request().get(String.class));
    }

    // 9.2.4
    @Test
    public void testRequest() {
        assertEquals("ok", target("/request").request().get(String.class));
    }

    // 9.2.5
    @Test
    public void testSecurityContext() {
        assertEquals("ok", target("/securitycontext").request().get(String.class));
    }

    // 9.2.6
    @Test
    public void testProviders() {
        assertEquals("ok", target("/providers").request().get(String.class));
    }

    // 9.2.7
    @Test
    public void testResourceContext() {
        assertEquals("ok", target("/resourcecontext").request().get(String.class));
    }

    // 9.2.8
    @Test
    public void testConfiguration() {
        assertEquals("ok", target("/configuration").request().get(String.class));
    }

    @Test
    public void testServletConfig() {
        assertEquals("ok", target("/servletconfig").request().get(String.class));
    }

    @Test
    public void testServletContext() {
        assertEquals("ok", target("/servletcontext").request().get(String.class));
    }

    @Test
    public void testHttpServletRequest() {
        assertEquals("ok", target("/httpservletrequest").request().get(String.class));
    }

    @Test
    public void testHttpServletResponse() {
        assertEquals("ok", target("/httpservletresponse").request().get(String.class));
    }

    @Test
    public void testUnknown() {
        assertEquals(500, target("/unknown").request().get().getStatus());
    }
}
