package org.minijax.rs.delegates;

import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

import org.minijax.rs.util.CacheControlUtils;

class MinijaxCacheControlDelegate implements HeaderDelegate<CacheControl> {

    @Override
    public CacheControl fromString(final String value) {
        return CacheControlUtils.fromString(value);
    }

    @Override
    public String toString(final CacheControl value) {
        return CacheControlUtils.toString(value);
    }
}
