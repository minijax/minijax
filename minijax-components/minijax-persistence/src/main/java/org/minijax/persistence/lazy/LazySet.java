package org.minijax.persistence.lazy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.minijax.persistence.MinijaxBaseTypedQuery;

public class LazySet<E> implements Set<E> {
    private final MinijaxBaseTypedQuery<E> query;
    private Set<E> data;

    public LazySet(final MinijaxBaseTypedQuery<E> query) {
        this.query = query;
    }

    private Set<E> getData() {
        if (data == null) {
            data = new HashSet<>(query.getResultList());
        }
        return data;
    }

    @Override
    public int size() {
        return getData().size();
    }

    @Override
    public boolean isEmpty() {
        return getData().isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return getData().contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return getData().iterator();
    }

    @Override
    public Object[] toArray() {
        return getData().toArray();
    }

    @Override
    public <X> X[] toArray(final X[] a) {
        return getData().toArray(a);
    }

    @Override
    public boolean add(final E e) {
        return getData().add(e);
    }

    @Override
    public boolean remove(final Object o) {
        return getData().remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return getData().containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        return getData().addAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return getData().retainAll(c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return getData().removeAll(c);
    }

    @Override
    public void clear() {
        getData().clear();
    }
}
