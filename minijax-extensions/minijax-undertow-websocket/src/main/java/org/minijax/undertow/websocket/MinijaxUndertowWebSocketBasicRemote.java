package org.minijax.undertow.websocket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;

import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;

public class MinijaxUndertowWebSocketBasicRemote implements RemoteEndpoint.Basic {
    private final WebSocketChannel channel;

    public MinijaxUndertowWebSocketBasicRemote(final WebSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void sendText(final String text) throws IOException {
        WebSockets.sendText(text, channel, null);
    }

    /*
     * Unsupported
     */

    @Override
    public void setBatchingAllowed(final boolean allowed) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getBatchingAllowed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flushBatch() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendPing(final ByteBuffer applicationData) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendPong(final ByteBuffer applicationData) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendBinary(final ByteBuffer data) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendText(final String partialMessage, final boolean isLast) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendBinary(final ByteBuffer partialByte, final boolean isLast) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public OutputStream getSendStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Writer getSendWriter() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendObject(final Object data) throws IOException, EncodeException {
        throw new UnsupportedOperationException();
    }
}
