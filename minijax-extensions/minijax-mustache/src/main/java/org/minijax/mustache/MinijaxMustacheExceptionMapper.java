package org.minijax.mustache;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.minijax.util.ExceptionUtils;

@Provider
@Produces(MediaType.TEXT_HTML)
public class MinijaxMustacheExceptionMapper implements ExceptionMapper<Exception> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(final Exception exception) {
        final WebApplicationException webAppException = ExceptionUtils.toWebAppException(exception);

        String message = webAppException.getMessage();
        if (message != null && message.startsWith("HTTP ")) {
            message = message.substring(5);
        }

        final int status = webAppException.getResponse().getStatus();
        final View view = new View("error");
        view.getProps().put("message", message);
        return Response.status(status).type(MediaType.TEXT_HTML_TYPE).entity(view).build();
    }
}
