package org.minijax.persistence;

import java.util.List;

import javax.persistence.criteria.Selection;

import org.minijax.commons.MinijaxException;

public class MinijaxSelection<X> implements javax.persistence.criteria.Selection<X> {
    private final Class<X> javaType;
    private String alias;

    public MinijaxSelection(final Class<X> javaType) {
        this.javaType = javaType;
    }

    @Override
    public Class<X> getJavaType() {
        return javaType;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public Selection<X> alias(final String name) {
        if (this.alias != null) {
            throw new MinijaxException("Alias already set");
        }
        this.alias = name;
        return this;
    }

    @Override
    public boolean isCompoundSelection() {
        return false;
    }

    /*
     * Unsupported
     */

    @Override
    public List<Selection<?>> getCompoundSelectionItems() {
        throw new UnsupportedOperationException();
    }
}
