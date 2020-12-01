package org.minijax.rs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import org.minijax.rs.util.UrlUtils;

@Provider
@Singleton
class MinijaxCorsFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private final List<String> pathPrefixes = new ArrayList<>();

    public void addPathPrefix(final String pathPrefix) {
        pathPrefixes.add(pathPrefix);
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        if (requestContext.getMethod().equals("OPTIONS")) {
            requestContext.abortWith(Response.ok().build());
        }
    }

    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext)
            throws IOException {

        if (isCorsAllowed(requestContext)) {
            allowCors(requestContext, responseContext.getHeaders());
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
    private static void allowCors(final ContainerRequestContext request, final MultivaluedMap<String, Object> responseHeaders)  {
        final String origin = request.getHeaderString("Origin");
        if (origin == null) {
            return;
        }

        responseHeaders.add("Access-Control-Allow-Origin", origin);
        responseHeaders.add("Access-Control-Allow-Credentials", "true");
        responseHeaders.add("Access-Control-Allow-Methods", "GET, HEAD, OPTIONS, PATCH, POST, PUT, DELETE");

        final String headers = request.getHeaderString("Access-Control-Request-Headers");
        if (headers != null) {
            responseHeaders.add("Access-Control-Allow-Headers", UrlUtils.urlDecode(headers));
        }
    }
}
