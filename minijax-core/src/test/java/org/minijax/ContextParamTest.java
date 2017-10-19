package org.minijax;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class ContextParamTest extends MinijaxTest {

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
    @Path("/requestcontext")
    public static String getRequestContext(@Context final ContainerRequestContext context) {
        return "ok";
    }

    @GET
    @Path("/uriinfo")
    public static String getUriInfo(@Context final UriInfo uriInfo) {
        return "ok";
    }

    @POST
    @Path("/minijaxform")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public static String getMinijaxForm(@Context final MinijaxForm form) {
        return "ok";
    }

    @GET
    @Path("/unknown")
    public static String getUnknown(@Context final Object obj) {
        return null;
    }

    @Before
    public void setUp() {
        register(ContextParamTest.class);
    }

    @Test
    @Ignore("Refactor MockRequestContext to use servlet requests")
    public void testHttpServletRequest() {
        assertEquals("ok", target("/httpservletrequest").request().get(String.class));
    }

    @Test
    @Ignore("Refactor MockRequestContext to use servlet responses")
    public void testHttpServletRespons() {
        assertEquals("ok", target("/httpservletresponse").request().get(String.class));
    }

    @Test
    public void testRequestContext() {
        assertEquals("ok", target("/requestcontext").request().get(String.class));
    }

    @Test
    public void testUriInfo() {
        assertEquals("ok", target("/uriinfo").request().get(String.class));
    }

    @Test
    public void testMinijaxForm() {
        assertEquals("ok", target("/minijaxform").request().post(Entity.form(new Form()), String.class));
    }

    @Test
    public void testUnknown() {
        assertEquals(500, target("/unknown").request().get().getStatus());
    }
}
