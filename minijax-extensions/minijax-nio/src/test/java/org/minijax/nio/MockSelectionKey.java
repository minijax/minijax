package org.minijax.nio;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public class MockSelectionKey extends SelectionKey {
    private final MockSelector selector;
    private final SelectableChannel channel;
    private int interestOps;
    private int readyOps;

    public MockSelectionKey(final MockSelector selector, final SelectableChannel channel, final int interestOps, final Object attachment) {
        this.selector = selector;
        this.channel = channel;
        this.interestOps = interestOps;
        this.attach(attachment);
    }

    @Override
    public Selector selector() {
        return selector;
    }

    @Override
    public SelectableChannel channel() {
        return channel;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void cancel() {
    }

    @Override
    public int interestOps() {
        return interestOps;
    }

    @Override
    public SelectionKey interestOps(final int ops) {
        interestOps = ops;
        return this;
    }

    @Override
    public int readyOps() {
        return readyOps;
    }
}
