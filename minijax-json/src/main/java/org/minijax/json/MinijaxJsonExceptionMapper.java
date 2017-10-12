package org.minijax.json;

import javax.inject.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.minijax.util.ExceptionUtils;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class MinijaxJsonExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(final Exception exception) {
        final WebApplicationException webAppEx = ExceptionUtils.toWebAppException(exception);
        return Response
                .status(webAppEx.getResponse().getStatus())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new MinijaxJsonExceptionWrapper(webAppEx))
                .build();
    }
}
