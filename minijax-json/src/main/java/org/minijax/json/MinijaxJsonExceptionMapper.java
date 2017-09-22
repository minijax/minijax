package org.minijax.json;

import javax.inject.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class MinijaxJsonExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(final WebApplicationException exception) {
        return Response
                .status(exception.getResponse().getStatus())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new MinijaxJsonExceptionWrapper(exception))
                .build();
    }
}
