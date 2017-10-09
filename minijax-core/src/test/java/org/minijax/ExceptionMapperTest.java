package org.minijax;

import static org.junit.Assert.*;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.junit.Before;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class ExceptionMapperTest extends MinijaxTest {

    @Produces(MediaType.TEXT_PLAIN)
    public static class MyMapper implements ExceptionMapper<Exception> {

        @Override
        public Response toResponse(final Exception exception) {
            if (exception instanceof NotFoundException) {
                return Response.status(404).entity("Sorry, not found").build();
            }
            throw new UnsupportedOperationException();
        }
    }


    @GET
    @Path("/notfound")
    @Produces(MediaType.TEXT_PLAIN)
    public static Response throwNotFound() {
        throw new NotFoundException();
    }


    @Before
    public void setUp() {
        register(ExceptionMapperTest.class);
        register(MyMapper.class);
    }


    @Test
    public void testNotFound() {
        assertEquals("Sorry, not found", target("/notfound").request().get().getEntity());
    }
}
