package org.minijax;

import static org.junit.Assert.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.junit.Test;
import org.minijax.InnerClassTest.InnerClass.InnerInnerClass;
import org.minijax.test.MinijaxTest;

public class InnerClassTest extends MinijaxTest {

    public class InnerClass {
        @GET
        @Path("innerclass")
        public String getInnerClass() {
            return "Inner class!";
        }

        public class InnerInnerClass {
            @GET
            @Path("innerinnerclass")
            public String getInnerInnerClass() {
                return "Inner inner class!";
            }
        }
    }


    @Test
    public void testInnerClass() {
        register(InnerClass.class);

        assertEquals(
                "Inner class!",
                target("/innerclass").request().get(String.class));
    }


    @Test
    public void testInnerInnerClass() {
        register(InnerInnerClass.class);

        assertEquals(
                "Inner inner class!",
                target("/innerinnerclass").request().get(String.class));
    }
}
