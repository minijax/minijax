package org.minijax.nio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

public class MockServerSocketChannel extends ServerSocketChannel {
    private final ServerSocket socket;

    protected MockServerSocketChannel(final SelectorProvider provider) throws IOException {
        super(provider);
        socket = new ServerSocket();
    }

    @Override
    public ServerSocket socket() {
        return socket;
    }

    @Override
    protected void implConfigureBlocking(final boolean block) throws IOException {
    }

    @Override
    public SocketChannel accept() throws IOException {
        return new MockSocketChannel(provider());
    }

    // Unsupported

    @Override
    public <T> T getOption(final SocketOption<T> name) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<SocketOption<?>> supportedOptions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServerSocketChannel bind(final SocketAddress local, final int backlog) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> ServerSocketChannel setOption(final SocketOption<T> name, final T value) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SocketAddress getLocalAddress() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void implCloseSelectableChannel() throws IOException {
        throw new UnsupportedOperationException();
    }
}
