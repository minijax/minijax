package org.minijax.rs.cdi;

import static org.junit.Assert.*;

import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.UriInfo;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.rs.test.MinijaxTest;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

public class InjectTest extends MinijaxTest {

    @Singleton
    static class Counter {
        int count;
    }

    public static class MyBean {
        @PathParam("a") String path;
    }

    public static class InjectedResource {
        @Inject Counter counter;
        @Context UriInfo uriInfo;
        @CookieParam("a") String cookie;
        @FormParam("a") String form;
        @HeaderParam("a") String header;
        @QueryParam("a") @DefaultValue("b") String query;
        @PathParam("a") String path;
        @BeanParam MyBean bean;

        @GET
        @POST
        @Path("/inject/{a}")
        public InjectedResource get() {
            return this;
        }
    }

    @BeforeClass
    public static void setUpInjectTest() {
        resetServer();
        register(Counter.class);
        register(InjectedResource.class);
    }

    @Test
    public void testGetCounter() {
        final InjectedResource r = target("/inject/test").request().get(InjectedResource.class);
        assertNotNull(r.counter);
    }

    @Test
    public void testUriInfo() {
        final InjectedResource r = target("/inject/test").request().get(InjectedResource.class);
        assertEquals("/inject/test", r.uriInfo.getPath());
    }

    @Test
    public void testCookie() {
        final InjectedResource r = target("/inject/test").request().cookie("a", "hello").get(InjectedResource.class);
        assertNotNull(r);
        assertEquals("hello", r.cookie);
    }

    @Test
    public void testFormParam() {
        final Form form = new Form();
        form.param("a", "hello");

        final InjectedResource r = target("/inject/test").request().post(Entity.form(form), InjectedResource.class);
        assertEquals("hello", r.form);
    }

    @Test
    public void testHeader() {
        final InjectedResource r = target("/inject/test").request().header("a", "myheader").get(InjectedResource.class);
        assertEquals("myheader", r.header);
    }

    @Test
    public void testQuery() {
        final InjectedResource r = target("/inject/test?a=myquery").request().get(InjectedResource.class);
        assertEquals("myquery", r.query);
    }

    @Test
    public void testQueryDefaultValue() {
        final InjectedResource r = target("/inject/test").request().get(InjectedResource.class);
        assertEquals("b", r.query);
    }

    @Test
    public void testPath() {
        final InjectedResource r = target("/inject/mypath").request().get(InjectedResource.class);
        assertEquals("mypath", r.path);
    }

    @Test
    public void testBean() {
        final InjectedResource r = target("/inject/mypath").request().get(InjectedResource.class);
        assertEquals("mypath", r.bean.path);
    }
}
