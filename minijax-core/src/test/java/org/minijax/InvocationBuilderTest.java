package org.minijax;

import static org.junit.Assert.*;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericType;

import org.junit.Before;
import org.junit.Test;
import org.minijax.test.MinijaxInvocationBuilder;
import org.minijax.test.MinijaxTest;

public class InvocationBuilderTest extends MinijaxTest {

    @Path("/")
    @GET @POST @PUT @DELETE @OPTIONS
    public static String endpoint() {
        return "ok";
    }

    @Before
    public void setUp() {
        register(InvocationBuilderTest.class);
    }

    @Test
    public void testHead() {
        assertNotNull(target("/").request().head());
    }

    @Test
    public void testOptions() {
        assertNotNull(target("/").request().options());
    }

    @Test
    public void testOptionsClass() {
        assertNotNull(target("/").request().options(Object.class));
    }

    @Test
    public void testOptionsGenericType() {
        assertNotNull(target("/").request().options(new GenericType<Object>() {}));
    }

    @Test
    public void testGet() {
        assertNotNull(target("/").request().get());
    }

    @Test
    public void testGetClass() {
        assertNotNull(target("/").request().get(Object.class));
    }

    @Test
    public void testGetGenericType() {
        assertNotNull(target("/").request().get(new GenericType<Object>() {}));
    }

    @Test
    public void testDelete() {
        assertNotNull(target("/").request().delete());
    }

    @Test
    public void testDeleteClass() {
        assertNotNull(target("/").request().delete(Object.class));
    }

    @Test
    public void testDeleteGenericType() {
        assertNotNull(target("/").request().delete(new GenericType<Object>() {}));
    }

    @Test
    public void testPost() {
        assertNotNull(target("/").request().post(null));
    }

    @Test
    public void testPostClass() {
        assertNotNull(target("/").request().post(null, Object.class));
    }

    @Test
    public void testPostGenericType() {
        assertNotNull(target("/").request().post(null, new GenericType<Object>() {}));
    }

    @Test
    public void testPut() {
        assertNotNull(target("/").request().put(null));
    }

    @Test
    public void testPutClass() {
        assertNotNull(target("/").request().put(null, Object.class));
    }

    @Test
    public void testPutGenericType() {
        assertNotNull(target("/").request().put(null, new GenericType<Object>() {}));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTrace() {
        target("/").request().trace();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTraceClass() {
        target("/").request().trace(Object.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTraceGenericType() {
        target("/").request().trace(new GenericType<Object>() {});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBuild() {
        new MinijaxInvocationBuilder(null).build(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBuild2() {
        new MinijaxInvocationBuilder(null).build(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBuildGet() {
        new MinijaxInvocationBuilder(null).buildGet();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBuildDelete() {
        new MinijaxInvocationBuilder(null).buildDelete();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBuildPost() {
        new MinijaxInvocationBuilder(null).buildPost(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBuildPut() {
        new MinijaxInvocationBuilder(null).buildPut(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRx() {
        new MinijaxInvocationBuilder(null).rx();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRxClass() {
        new MinijaxInvocationBuilder(null).rx(null);
    }
}
