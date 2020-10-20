package org.minijax.jndi;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.Binding;
import javax.naming.NamingEnumeration;

public class MinijaxBindingsEnumeration implements NamingEnumeration<Binding> {
    private final Iterator<Entry<Object, Object>> iterator;

    public MinijaxBindingsEnumeration(final Map<Object, Object> data) {
        this.iterator = data.entrySet().iterator();
    }

    @Override
    public Binding next() {
        final Entry<Object, Object> entry = iterator.next();
        return new Binding(entry.getKey().toString(), entry.getValue());
    }

    @Override
    public boolean hasMore() {
        return iterator.hasNext();
    }

    @Override
    public void close() {
        // Nothing to do
    }

    @Override
    public Binding nextElement() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasMoreElements() {
        throw new UnsupportedOperationException();
    }
}
