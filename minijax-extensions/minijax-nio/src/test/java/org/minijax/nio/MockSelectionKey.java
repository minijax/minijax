package org.minijax.nio;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public class MockSelectionKey extends SelectionKey {
    private final int readyOps;

    public MockSelectionKey(final int readyOps) {
        this.readyOps = readyOps;
    }

    @Override
    public SelectableChannel channel() {
        return new MockSocketChannel(null);
    }

    @Override
    public Selector selector() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isValid() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void cancel() {
        // TODO Auto-generated method stub

    }

    @Override
    public int interestOps() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public SelectionKey interestOps(final int ops) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int readyOps() {
        return readyOps;
    }
}
