package org.minijax.persistence;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.Selection;

public class MinijaxSelection<X> implements javax.persistence.criteria.Selection<X> {
    private final Class<? extends X> javaType;
    private String alias;

    public MinijaxSelection(final Class<? extends X> javaType) {
        this.javaType = javaType;
    }

    @Override
    public Class<? extends X> getJavaType() {
        return javaType;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public Selection<X> alias(final String name) {
        if (this.alias != null) {
            throw new PersistenceException("Alias already set");
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
