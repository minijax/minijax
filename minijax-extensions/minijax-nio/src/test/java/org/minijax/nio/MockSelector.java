package org.minijax.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MockSelector extends AbstractSelector {

    protected MockSelector(final SelectorProvider provider) {
        super(provider);
    }

    @Override
    protected void implCloseSelector() throws IOException {
    }

    @Override
    protected SelectionKey register(final AbstractSelectableChannel ch, final int ops, final Object att) {
        return null;
    }

    @Override
    public Set<SelectionKey> keys() {
        return Collections.emptySet();
    }

    @Override
    public Set<SelectionKey> selectedKeys() {
        return new HashSet<>(Arrays.asList(
                new MockSelectionKey(0),
                new MockSelectionKey(SelectionKey.OP_ACCEPT),
                new MockSelectionKey(SelectionKey.OP_READ)
                ));
    }

    @Override
    public int selectNow() throws IOException {
        return 1;
    }

    @Override
    public int select(final long timeout) throws IOException {
        return 1;
    }

    @Override
    public int select() throws IOException {
        return 1;
    }

    @Override
    public Selector wakeup() {
        return null;
    }
}
