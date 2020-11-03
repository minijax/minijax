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

public class InvocationBuilderTest extends MinijaxTest {

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
    public void testHead() {
        assertNotNull(target("/").request().head());
    }

    @Test
    public void testOptions() {
        assertNotNull(target("/").request().options());
    }

    @Test
    public void testOptionsClass() {
        assertNotNull(target("/").request().options(Object.class));
    }

    @Test
    public void testOptionsGenericType() {
        assertNotNull(target("/").request().options(new GenericType<Object>() {
        }));
    }

    @Test
    public void testGet() {
        assertNotNull(target("/").request().get());
    }

    @Test
    public void testGetClass() {
        assertNotNull(target("/").request().get(Object.class));
    }

    @Test
    public void testGetGenericType() {
        assertNotNull(target("/").request().get(new GenericType<Object>() {
        }));
    }

    @Test
    public void testDelete() {
        assertNotNull(target("/").request().delete());
    }

    @Test
    public void testDeleteClass() {
        assertNotNull(target("/").request().delete(Object.class));
    }

    @Test
    public void testDeleteGenericType() {
        assertNotNull(target("/").request().delete(new GenericType<Object>() {
        }));
    }

    @Test
    public void testPost() {
        assertNotNull(target("/").request().post(null));
    }

    @Test
    public void testPostClass() {
        assertNotNull(target("/").request().post(null, Object.class));
    }

    @Test
    public void testPostGenericType() {
        assertNotNull(target("/").request().post(null, new GenericType<Object>() {
        }));
    }

    @Test
    public void testPut() {
        assertNotNull(target("/").request().put(null));
    }

    @Test
    public void testPutClass() {
        assertNotNull(target("/").request().put(null, Object.class));
    }

    @Test
    public void testPutGenericType() {
        assertNotNull(target("/").request().put(null, new GenericType<Object>() {
        }));
    }

    @Test
    public void testTrace() {
        assertThrows(UnsupportedOperationException.class, () -> target("/").request().trace());
    }

    @Test
    public void testTraceClass() {
        assertThrows(UnsupportedOperationException.class, () -> target("/").request().trace(Object.class));
    }

    @Test
    public void testTraceGenericType() {
        assertThrows(UnsupportedOperationException.class, () -> target("/").request().trace(new GenericType<Object>() {
        }));
    }

    @Test
    public void testBuild() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxTestInvocationBuilder(null).build(null));
    }

    @Test
    public void testBuild2() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxTestInvocationBuilder(null).build(null, null));
    }

    @Test
    public void testBuildGet() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxTestInvocationBuilder(null).buildGet());
    }

    @Test
    public void testBuildDelete() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxTestInvocationBuilder(null).buildDelete());
    }

    @Test
    public void testBuildPost() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxTestInvocationBuilder(null).buildPost(null));
    }

    @Test
    public void testBuildPut() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxTestInvocationBuilder(null).buildPut(null));
    }

    @Test
    public void testRx() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxTestInvocationBuilder(null).rx());
    }

    @Test
    public void testRxClass() {
        assertThrows(UnsupportedOperationException.class, () -> new MinijaxTestInvocationBuilder(null).rx(null));
    }
}
