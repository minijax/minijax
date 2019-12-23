package org.minijax.nio;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

public class MockSocketChannel extends SocketChannel {

    protected MockSocketChannel(final SelectorProvider provider) {
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
    public SocketChannel bind(final SocketAddress local) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> SocketChannel setOption(final SocketOption<T> name, final T value) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SocketChannel shutdownInput() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SocketChannel shutdownOutput() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Socket socket() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isConnected() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isConnectionPending() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean connect(final SocketAddress remote) throws IOException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean finishConnect() throws IOException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public SocketAddress getRemoteAddress() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long read(final ByteBuffer[] dsts, final int offset, final int length) throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long write(final ByteBuffer[] srcs, final int offset, final int length) throws IOException {
        // TODO Auto-generated method stub
        return 0;
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
