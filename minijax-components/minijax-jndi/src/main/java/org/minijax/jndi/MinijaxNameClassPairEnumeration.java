package org.minijax.jndi;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;

public class MinijaxNameClassPairEnumeration implements NamingEnumeration<NameClassPair> {
    private final Iterator<Entry<Object, Object>> iterator;

    public MinijaxNameClassPairEnumeration(final Map<Object, Object> data) {
        this.iterator = data.entrySet().iterator();
    }

    @Override
    @SuppressWarnings("unchecked")
    public NameClassPair next() {
        final Entry<Object, Object> entry = iterator.next();
        return new NameClassPair(entry.getKey().toString(), entry.getValue().getClass().getName());
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
    public NameClassPair nextElement() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasMoreElements() {
        throw new UnsupportedOperationException();
    }
}
