package org.minijax.mustache;

import static jakarta.ws.rs.core.MediaType.*;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.minijax.rs.delegates.MinijaxResponseBuilder;
import org.minijax.rs.util.ExceptionUtils;
import org.minijax.view.View;

@Provider
@Produces(TEXT_HTML)
public class MinijaxMustacheExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(final Exception exception) {
        final WebApplicationException webAppException = ExceptionUtils.toWebAppException(exception);

        String message = webAppException.getMessage();
        if (message != null && message.startsWith("HTTP ")) {
            message = message.substring(5);
        }

        final int status = webAppException.getResponse().getStatus();
        final View view = new View("error");
        view.getModel().put("message", message);
        return new MinijaxResponseBuilder().status(status).type(TEXT_HTML_TYPE).entity(view).build();
    }
}
