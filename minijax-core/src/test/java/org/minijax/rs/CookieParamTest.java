package org.minijax.rs;

import static org.junit.Assert.*;

import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.rs.test.MinijaxTest;

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
