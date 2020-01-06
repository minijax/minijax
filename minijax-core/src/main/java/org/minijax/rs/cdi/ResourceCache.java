package org.minijax.rs.cdi;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;

import org.minijax.rs.util.CloseUtils;

/**
 * The ResourceCache maps a CDI <code>Key</code> to instances of the class.
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
