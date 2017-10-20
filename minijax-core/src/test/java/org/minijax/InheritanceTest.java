package org.minijax;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class InheritanceTest extends MinijaxTest {

    public static abstract class MyBaseClass {
        public boolean baseInjected;

        @Inject
        public void baseInject() {
            baseInjected = true;
        }
    }

    @Singleton
    public static class MySubClass extends MyBaseClass {
        public boolean subInjected;

        @Inject
        public void subInject() {
            subInjected = true;
        }
    }


    public static abstract class BasePathParamClass {
        @PathParam("id")
        public String id;
    }


    public static class SubPathParamClass extends BasePathParamClass {
        @GET
        @Path("/{id}")
        public String handle() {
            return id;
        }
    }


    @Test
    public void testInheritanceInject() {
        register(MySubClass.class);

        final MySubClass r = getServer().get(MySubClass.class, null, null);
        assertNotNull(r);
        assertTrue(r.subInjected);
        assertTrue(r.baseInjected);
    }


    @Test
    public void testInheritancePathParam() {
        register(SubPathParamClass.class);

        assertEquals("foo", target("/foo").request().get(String.class));
    }
}
