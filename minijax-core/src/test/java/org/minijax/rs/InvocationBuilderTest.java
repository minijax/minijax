package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.GenericType;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;
import org.minijax.rs.test.MinijaxTestInvocationBuilder;

class InvocationBuilderTest extends MinijaxTest {

    @Path("/")
    @GET
    @POST
    @PUT
    @DELETE
    @OPTIONS
    public static String endpoint() {
        return "ok";
    }

    @BeforeAll
    public static void setUpInvocationBuilderTest() {
        resetServer();
        register(InvocationBuilderTest.class);
    }

    @Test
    void testHead() {
        assertNotNull(target("/").request().head());
    }

    @Test
    void testOptions() {
        assertNotNull(target("/").request().options());
    }

    @Test
    void testOptionsClass() {
        assertNotNull(target("/").request().options(Object.class));
    }

    @Test
    void testOptionsGenericType() {
        assertNotNull(target("/").request().options(new GenericType<Object>() {
        }));
    }

    @Test
    void testGet() {
        assertNotNull(target("/").request().get());
    }

    @Test
    void testGetClass() {
        assertNotNull(target("/").request().get(Object.class));
    }

    @Test
    void testGetGenericType() {
        assertNotNull(target("/").request().get(new GenericType<Object>() {
        }));
    }

    @Test
    void testDelete() {
        assertNotNull(target("/").request().delete());
    }

    @Test
    void testDeleteClass() {
        assertNotNull(target("/").request().delete(Object.class));
    }

    @Test
    void testDeleteGenericType() {
        assertNotNull(target("/").request().delete(new GenericType<Object>() {
        }));
    }

    @Test
    void testPost() {
        assertNotNull(target("/").request().post(null));
    }

    @Test
    void testPostClass() {
        assertNotNull(target("/").request().post(null, Object.class));
    }

    @Test
    void testPostGenericType() {
        assertNotNull(target("/").request().post(null, new GenericType<Object>() {
        }));
    }

    @Test
    void testPut() {
        assertNotNull(target("/").request().put(null));
    }

    @Test
    void testPutClass() {
        assertNotNull(target("/").request().put(null, Object.class));
    }

    @Test
    void testPutGenericType() {
        assertNotNull(target("/").request().put(null, new GenericType<Object>() {
        }));
    }

    @Test
    void testTrace() {
        assertThrows(UnsupportedOperationException.class, () -> target("/").request().trace());
    }

    @Test
    void testTraceClass() {
        assertThrows(UnsupportedOperationException.class, () -> target("/").request().trace(Object.class));
    }

    @Test
    void testTraceGenericType() {
        assertThrows(UnsupportedOperationException.class, () -> target("/").request().trace(new GenericType<Object>() {
        }));
    }

    @Test
    void testBuild() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxTestInvocationBuilder(null).build(null));
    }

    @Test
    void testBuild2() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxTestInvocationBuilder(null).build(null, null));
    }

    @Test
    void testBuildGet() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxTestInvocationBuilder(null).buildGet());
    }

    @Test
    void testBuildDelete() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxTestInvocationBuilder(null).buildDelete());
    }

    @Test
    void testBuildPost() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxTestInvocationBuilder(null).buildPost(null));
    }

    @Test
    void testBuildPut() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxTestInvocationBuilder(null).buildPut(null));
    }

    @Test
    void testRx() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxTestInvocationBuilder(null).rx());
    }

    @Test
    void testRxClass() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxTestInvocationBuilder(null).rx(null));
    }
}
