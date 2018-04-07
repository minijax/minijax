package org.minijax;

import static javax.ws.rs.core.MediaType.*;

import static org.junit.Assert.*;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

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
}
