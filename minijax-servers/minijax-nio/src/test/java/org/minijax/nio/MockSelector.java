package org.minijax.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashSet;
import java.util.Set;

public class MockSelector extends AbstractSelector {
    private final Set<SelectionKey> keys;
    private final Set<SelectionKey> selectedKeys;

    protected MockSelector(final SelectorProvider provider) {
        super(provider);
        this.keys = new HashSet<>();
        this.selectedKeys = new HashSet<>();
    }

    @Override
    protected void implCloseSelector() throws IOException {
    }

    @Override
    protected SelectionKey register(final AbstractSelectableChannel ch, final int ops, final Object att) {
        final MockSelectionKey key = new MockSelectionKey(this, ch, ops, att);
        this.keys.add(key);
        return key;
    }

    @Override
    public Set<SelectionKey> keys() {
        return this.keys;
    }

    @Override
    public Set<SelectionKey> selectedKeys() {
        return this.selectedKeys;
    }

    @Override
    public int selectNow() throws IOException {
        this.selectedKeys.addAll(this.keys);
        return this.selectedKeys.size();
    }

    @Override
    public int select(final long timeout) throws IOException {
        return selectNow();
    }

    @Override
    public int select() throws IOException {
        return selectNow();
    }

    @Override
    public Selector wakeup() {
        return this;
    }
}
