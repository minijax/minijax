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
    private final Socket socket = new Socket();
    private final ByteBuffer inputBuffer;
    private final ByteBuffer outputBuffer;

    protected MockSocketChannel(final SelectorProvider provider) {
        super(provider);
        inputBuffer = ByteBuffer.allocate(1000);
        outputBuffer = ByteBuffer.allocate(1000);
    }

    public MockSocketChannel(final SelectorProvider provider, final String input) {
        this(provider);
        inputBuffer.put(input.getBytes());
        inputBuffer.flip();
    }

    @Override
    public Socket socket() {
        return socket;
    }

    @Override
    protected void implConfigureBlocking(final boolean block) throws IOException {
    }

    @Override
    protected void implCloseSelectableChannel() throws IOException {
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        final int bytesRead = inputBuffer.remaining();
        dst.put(inputBuffer);
        return bytesRead;
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        final int bytesWritten = src.remaining();
        outputBuffer.put(src);
        return bytesWritten;
    }

    public String getOutputAsString() {
        outputBuffer.flip();
        final StringBuilder b = new StringBuilder();
        while (outputBuffer.hasRemaining()) {
            b.append((char) outputBuffer.get());
        }
        return b.toString();
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
    public SocketChannel bind(final SocketAddress local) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> SocketChannel setOption(final SocketOption<T> name, final T value) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SocketChannel shutdownInput() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SocketChannel shutdownOutput() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isConnected() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isConnectionPending() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean connect(final SocketAddress remote) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean finishConnect() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SocketAddress getRemoteAddress() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long read(final ByteBuffer[] dsts, final int offset, final int length) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long write(final ByteBuffer[] srcs, final int offset, final int length) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SocketAddress getLocalAddress() throws IOException {
        throw new UnsupportedOperationException();
    }
}
