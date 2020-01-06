package org.minijax.security;

import static javax.ws.rs.HttpMethod.*;
import static javax.ws.rs.core.MediaType.*;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.minijax.rs.MinijaxRequestContext;

@RequestScoped
class CsrfFilter implements ContainerRequestFilter {

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        if (!requestContext.getMethod().equals(POST)) {
            return;
        }

        final MinijaxRequestContext ctx = (MinijaxRequestContext) requestContext;
        final Security<?> security = ctx.getResource(Security.class);
        if (!security.isLoggedIn()) {
            return;
        }

        final String scheme = security.getAuthenticationScheme();
        if (scheme == null || !scheme.equals(SecurityContext.FORM_AUTH)) {
            return;
        }

        final MediaType contentType = ctx.getMediaType();
        if (contentType == null) {
            return;
        }

        if (!contentType.isCompatible(APPLICATION_FORM_URLENCODED_TYPE) &&
                !contentType.isCompatible(MULTIPART_FORM_DATA_TYPE)) {
            return;
        }

        security.validateSession(ctx.getForm().getString("csrf"));
    }
}
