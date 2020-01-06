package org.minijax.rs;

import static org.junit.Assert.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.rs.InnerClassTest.InnerClass.InnerInnerClass;
import org.minijax.rs.test.MinijaxTest;

public class InnerClassTest extends MinijaxTest {

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

    @BeforeClass
    public static void setUpInnerClassTest() {
        resetServer();
        register(InnerClass.class);
        register(InnerInnerClass.class);
    }

    @Test
    public void testInnerClass() {
        assertEquals(
                "Inner class!",
                target("/innerclass").request().get(String.class));
    }

    @Test
    public void testInnerInnerClass() {
        assertEquals(
                "Inner inner class!",
                target("/innerinnerclass").request().get(String.class));
    }
}
