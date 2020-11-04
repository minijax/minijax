package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.rs.InnerClassTest.InnerClass.InnerInnerClass;
import org.minijax.rs.test.MinijaxTest;

class InnerClassTest extends MinijaxTest {

    class InnerClass {
        @GET
        @Path("innerclass")
        public String getInnerClass() {
            return "Inner class!";
        }

        class InnerInnerClass {
            @GET
            @Path("innerinnerclass")
            public String getInnerInnerClass() {
                return "Inner inner class!";
            }
        }
    }

    @BeforeAll
    public static void setUpInnerClassTest() {
        resetServer();
        register(InnerClass.class);
        register(InnerInnerClass.class);
    }

    @Test
    void testInnerClass() {
        assertEquals(
                "Inner class!",
                target("/innerclass").request().get(String.class));
    }

    @Test
    void testInnerInnerClass() {
        assertEquals(
                "Inner inner class!",
                target("/innerinnerclass").request().get(String.class));
    }
}
