package org.minijax.rs.cdi;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;

import org.minijax.commons.CloseUtils;

/**
 * The ResourceCache maps a CDI <code>Key</code> to instances of the class.
 *
 * This class is not thread safe.  One instance should be used per request,
 * which should only be handled by one thread at a time.
 */
public class ResourceCache implements Closeable {
    private final Map<Object, Object> innerMap;

    public ResourceCache() {
        innerMap = new HashMap<>();
    }

    public <T> void put(final Object key, final T instance) {
        innerMap.put(key, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(final Object key) {
        return (T) innerMap.get(key);
    }

    @Override
    public void close() {
        CloseUtils.closeQuietly(innerMap.values());
    }
}
