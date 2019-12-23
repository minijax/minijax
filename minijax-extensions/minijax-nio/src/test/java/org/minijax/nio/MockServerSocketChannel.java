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

    protected MockServerSocketChannel(final SelectorProvider provider) {
        super(provider);
    }

    @Override
    public <T> T getOption(final SocketOption<T> name) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<SocketOption<?>> supportedOptions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ServerSocketChannel bind(final SocketAddress local, final int backlog) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> ServerSocketChannel setOption(final SocketOption<T> name, final T value) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ServerSocket socket() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SocketChannel accept() throws IOException {
        return new MockSocketChannel(null);
    }

    @Override
    public SocketAddress getLocalAddress() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void implCloseSelectableChannel() throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    protected void implConfigureBlocking(final boolean block) throws IOException {
        // TODO Auto-generated method stub

    }

}
