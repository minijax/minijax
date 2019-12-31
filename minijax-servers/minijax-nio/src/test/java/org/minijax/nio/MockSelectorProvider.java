package org.minijax.nio;

import java.io.IOException;
import java.net.ProtocolFamily;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Pipe;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;

public class MockSelectorProvider extends SelectorProvider {

    @Override
    public AbstractSelector openSelector() throws IOException {
        return new MockSelector(this);
    }

    @Override
    public ServerSocketChannel openServerSocketChannel() throws IOException {
        return new MockServerSocketChannel(this);
    }

    @Override
    public SocketChannel openSocketChannel() throws IOException {
        return new MockSocketChannel(this);
    }

    // Unsupported

    @Override
    public DatagramChannel openDatagramChannel() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DatagramChannel openDatagramChannel(final ProtocolFamily family) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Pipe openPipe() throws IOException {
        throw new UnsupportedOperationException();
    }
}
