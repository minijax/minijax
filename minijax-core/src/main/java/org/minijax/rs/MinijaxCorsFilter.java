package org.minijax.rs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.minijax.rs.util.UrlUtils;

@Provider
@Singleton
class MinijaxCorsFilter implements ContainerResponseFilter {
    private final List<String> pathPrefixes = new ArrayList<>();

    public void addPathPrefix(final String pathPrefix) {
        pathPrefixes.add(pathPrefix);
    }

    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext)
            throws IOException {

        if (isCorsAllowed(requestContext)) {
            allowCors(requestContext, responseContext);
        }
    }

    private boolean isCorsAllowed(final ContainerRequestContext context) {
        final String path = context.getUriInfo().getPath();
        for (final String corsUrlPattern : pathPrefixes) {
            if (path.startsWith(corsUrlPattern)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the HTTP head "Access-Control-Allow-Origin: *" to enable
     * all cross domain requests.
     */
    private static void allowCors(final ContainerRequestContext request, final ContainerResponseContext response)  {
        final String origin = request.getHeaderString("Origin");
        if (origin == null) {
            return;
        }

        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        response.getHeaders().add("Access-Control-Allow-Credentials", "true");
        response.getHeaders().add("Access-Control-Allow-Methods", "GET, HEAD, OPTIONS, PATCH, POST, PUT, DELETE");

        final String headers = request.getHeaderString("Access-Control-Request-Headers");
        if (headers != null) {
            response.getHeaders().add("Access-Control-Allow-Headers", UrlUtils.urlDecode(headers));
        }
    }
}
