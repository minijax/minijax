package org.minijax.cdi;

import java.io.Closeable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.minijax.util.CloseUtils;

/**
 * The ResourceCache maps a CDI <code>Key</code> to instances of the class.
 */
public class ResourceCache implements Closeable {
    private final Map<Key<?>, Object> innerMap;

    public ResourceCache() {
        innerMap = new HashMap<>();
    }

    public <T> void put(final Key<? extends T> key, final T instance) {
        innerMap.put(key, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(final Key<? extends T> key) {
        return (T) innerMap.get(key);
    }

    private Collection<Object> values() {
        return innerMap.values();
    }

    @Override
    public void close() {
        CloseUtils.closeQuietly(values());
    }
}
