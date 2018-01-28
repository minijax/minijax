package org.minijax.uri;

import java.util.HashMap;

public class ImplicitTemplateMap extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    private final Object[] values;
    private int index;

    public ImplicitTemplateMap(final Object[] values) {
        this.values = values;
    }

    @Override
    public Object get(final Object key) {
        Object result = super.get(key);

        if (result == null) {
            result = values[index++];
            super.put(key.toString(), result);
        }

        return result;
    }
}
