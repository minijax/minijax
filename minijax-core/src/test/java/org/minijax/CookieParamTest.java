package org.minijax;

import static org.junit.Assert.*;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class CookieParamTest extends MinijaxTest {

    @GET
    @Path("/cookieparam")
    public static String getCookieParam(@CookieParam("test") final String test) {
        return test;
    }

    @BeforeClass
    public static void setUpCookieParamTest() {
        resetServer();
        register(CookieParamTest.class);
    }

    @Test
    public void testCookieParam() {
        assertEquals(
                "Hello",
                target("/cookieparam").request().cookie("test", "Hello").get(String.class));
    }
}
