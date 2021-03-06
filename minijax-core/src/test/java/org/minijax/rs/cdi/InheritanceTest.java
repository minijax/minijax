package org.minijax.rs.cdi;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

class InheritanceTest extends MinijaxTest {

    public static class MyResource {
    }

    public static abstract class MyBaseClass {
        @Inject MyResource baseInjected;
    }

    @Singleton
    public static class MySubClass extends MyBaseClass {
        @Inject MyResource subInjected;
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

    @BeforeAll
    public static void setUpInheritanceTest() {
        resetServer();
        register(MySubClass.class);
        register(SubPathParamClass.class);
    }

    @Test
    void testInheritanceInject() {
        final MySubClass r = getServer().getResource(MySubClass.class);
        assertNotNull(r);
        assertNotNull(r.subInjected);
        assertNotNull(r.baseInjected);
    }

    @Test
    void testInheritancePathParam() {
        assertEquals("foo", target("/foo").request().get(String.class));
    }
}
