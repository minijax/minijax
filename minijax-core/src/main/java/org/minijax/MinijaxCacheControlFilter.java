package org.minijax;

import static javax.ws.rs.core.HttpHeaders.*;

import javax.inject.Singleton;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.ext.Provider;

@Provider
@Singleton
class MinijaxCacheControlFilter implements ContainerResponseFilter {
    private final CacheControl defaultCacheControl;

    public MinijaxCacheControlFilter(final CacheControl defaultCacheControl) {
        this.defaultCacheControl = defaultCacheControl;
    }

    @Override
    public void filter(final ContainerRequestContext request, final ContainerResponseContext response) {
        if (response.getHeaders().get(CACHE_CONTROL) == null) {
            response.getHeaders().add(CACHE_CONTROL, defaultCacheControl);
        }
    }
}
