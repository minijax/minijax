package org.minijax.persistence;

import java.util.List;

import javax.persistence.criteria.Selection;

public class MinijaxCompoundSelection<X>
        extends MinijaxSelection<X>
        implements javax.persistence.criteria.CompoundSelection<X> {

    private final List<Selection<?>> selectionItems;

    public MinijaxCompoundSelection(final Class<X> javaType, final List<Selection<?>> selectionItems) {
        super(javaType);
        this.selectionItems = selectionItems;
    }

    @Override
    public boolean isCompoundSelection() {
        return true;
    }

    @Override
    public List<Selection<?>> getCompoundSelectionItems() {
        return selectionItems;
    }
}