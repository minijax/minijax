package org.minijax.rs;

import static jakarta.ws.rs.core.MediaType.*;

import static org.junit.Assert.*;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.rs.test.MinijaxTest;

public class ExceptionMapperTest extends MinijaxTest {

    public static class ExceptionA extends Exception {
        private static final long serialVersionUID = 1L;
    }

    public static class ExceptionB extends ExceptionA {
        private static final long serialVersionUID = 1L;
    }

    @Produces(TEXT_PLAIN)
    public static class NotFoundMapper implements ExceptionMapper<NotFoundException> {
        @Override
        public Response toResponse(final NotFoundException exception) {
            return Response.status(404).entity("Sorry, not found").build();
        }
    }

    @Produces(TEXT_PLAIN)
    public static class MapperA implements ExceptionMapper<ExceptionA> {
        @Override
        public Response toResponse(final ExceptionA exception) {
            return Response.status(200).entity("A").build();
        }
    }

    @GET
    @Path("/notfound")
    @Produces(TEXT_PLAIN)
    public static Response throwNotFound() {
        throw new NotFoundException();
    }

    @GET
    @Path("/throw_a")
    @Produces(TEXT_PLAIN)
    public static Response throwA() throws ExceptionA {
        throw new ExceptionA();
    }

    @GET
    @Path("/throw_b")
    @Produces(TEXT_PLAIN)
    public static Response throwB() throws ExceptionA {
        throw new ExceptionB();
    }

    @GET
    @Path("/throw_b_no_produces")
    public static Response throwBNoProduces() throws ExceptionB {
        throw new ExceptionB();
    }

    @BeforeClass
    public static void setUpExceptionMapperTest() {
        resetServer();
        register(ExceptionMapperTest.class);
        register(NotFoundMapper.class);
        register(MapperA.class);
    }

    @Test
    public void testNotFound() {
        assertEquals("Sorry, not found", target("/notfound").request().get().getEntity());
    }

    @Test
    public void testThrowA() {
        assertEquals("A", target("/throw_a").request().get().getEntity());
    }

    @Test
    public void testThrowB() {
        assertEquals("A", target("/throw_b").request().get().getEntity());
    }

    @Test
    public void testThrowBNoProduces() {
        final Response response = target("/throw_b_no_produces").request().get();
        assertEquals(500, response.getStatus());
    }
}
