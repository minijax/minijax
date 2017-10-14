package org.minijax.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The ClassMap maps <code>Class</code> objects to instances of the class.
 */
public class ClassMap {
    private final Map<Class<?>, Object> innerMap;

    public ClassMap() {
        innerMap = new HashMap<>();
    }

    public <T> void put(final Class<? extends T> c, final T instance) {
        innerMap.put(c, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(final Class<? extends T> c) {
        return (T) innerMap.get(c);
    }

    public Collection<Object> values() {
        return innerMap.values();
    }
}
