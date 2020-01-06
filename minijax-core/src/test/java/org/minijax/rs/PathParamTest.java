package org.minijax.rs;

import static org.junit.Assert.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.rs.test.MinijaxTest;

public class PathParamTest extends MinijaxTest {

    @GET
    @Path("/args")
    public static String getArgs() {
        return "nil";
    }

    @GET
    @Path("/args/{arg}")
    public static String getArgs(@PathParam("arg") final String arg) {
        return arg;
    }

    @GET
    @Path("/args/{arg1}/{arg2}")
    public static String getArgs(
            @PathParam("arg1") final String arg1,
            @PathParam("arg2") final String arg2) {
        return arg1 + "-" + arg2;
    }

    @BeforeClass
    public static void setUpPathParamTest() {
        resetServer();
        register(PathParamTest.class);
    }

    @Test
    public void testNoArgs() {
        assertEquals("nil", target("/args").request().get(String.class));
    }

    @Test
    public void testOneArgs() {
        assertEquals("foo", target("/args/foo").request().get(String.class));
    }

    @Test
    public void testTwoArgs() {
        assertEquals("foo-bar", target("/args/foo/bar").request().get(String.class));
    }
}
