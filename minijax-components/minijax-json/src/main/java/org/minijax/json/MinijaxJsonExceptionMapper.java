package org.minijax.json;

import static jakarta.ws.rs.core.MediaType.*;

import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

import org.minijax.rs.util.ExceptionUtils;

@Singleton
@Produces(APPLICATION_JSON)
public class MinijaxJsonExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(final Exception exception) {
        final WebApplicationException webAppEx = ExceptionUtils.toWebAppException(exception);
        return Response
                .status(webAppEx.getResponse().getStatus())
                .type(APPLICATION_JSON_TYPE)
                .entity(new MinijaxJsonExceptionWrapper(webAppEx))
                .build();
    }
}
