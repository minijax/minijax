package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

class CookieParamTest extends MinijaxTest {

    @GET
    @Path("/cookieparam")
    public static String getCookieParam(@CookieParam("test") final String test) {
        return test;
    }

    @BeforeAll
    public static void setUpCookieParamTest() {
        resetServer();
        register(CookieParamTest.class);
    }

    @Test
    void testCookieParam() {
        assertEquals(
                "Hello",
                target("/cookieparam").request().cookie("test", "Hello").get(String.class));
    }
}
