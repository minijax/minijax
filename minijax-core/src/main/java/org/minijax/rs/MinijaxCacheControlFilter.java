package org.minijax.rs;

import static jakarta.ws.rs.core.HttpHeaders.*;

import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.CacheControl;

@Singleton
public class MinijaxCacheControlFilter implements ContainerResponseFilter {
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
