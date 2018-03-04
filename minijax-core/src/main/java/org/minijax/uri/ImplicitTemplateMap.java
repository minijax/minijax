package org.minijax.uri;

import java.util.Arrays;
import java.util.HashMap;

public class ImplicitTemplateMap extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    private final transient Object[] templateValues;
    private int index;

    public ImplicitTemplateMap(final Object[] templateValues) {
        this.templateValues = templateValues;
    }

    @Override
    public Object get(final Object key) {
        Object result = super.get(key);

        if (result == null) {
            result = templateValues[index++];
            super.put(key.toString(), result);
        }

        return result;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(templateValues);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof ImplicitTemplateMap)) {
            return false;
        }

        final ImplicitTemplateMap other = (ImplicitTemplateMap) obj;
        return super.equals(other) && Arrays.equals(templateValues, other.templateValues);
    }
}
