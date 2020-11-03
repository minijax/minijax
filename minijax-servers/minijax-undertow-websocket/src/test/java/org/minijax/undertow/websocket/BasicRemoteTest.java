package org.minijax.undertow.websocket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import jakarta.websocket.EncodeException;

import org.junit.jupiter.api.Test;

import io.undertow.websockets.core.StreamSinkFrameChannel;
import io.undertow.websockets.core.WebSocketChannel;

public class BasicRemoteTest {

    @Test
    public void testSendTextString() throws IOException {
        final StreamSinkFrameChannel sink = mock(StreamSinkFrameChannel.class);
        when(sink.send(any())).thenReturn(true);
        when(sink.flush()).thenReturn(true);

        final WebSocketChannel channel = mock(WebSocketChannel.class);
        when(channel.send(any())).thenReturn(sink);

        final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(channel);
        remote.sendText("test");
    }

    @Test
    public void testSetBatchingAllowed() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.setBatchingAllowed(false);
        });
    }

    @Test
    public void testGetBatchingAllowed() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.getBatchingAllowed();
        });
    }

    @Test
    public void testFlushBatch() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.flushBatch();
        });
    }

    @Test
    public void testSendPing() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.sendPing(null);
        });
    }

    @Test
    public void testSendPong() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.sendPong(null);
        });
    }

    @Test
    public void testSendBinaryByteBuffer() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.sendBinary(null);
        });
    }

    @Test
    public void testSendTextStringBoolean() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.sendText(null, false);
        });
    }

    @Test
    public void testSendBinaryByteBufferBoolean() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.sendBinary(null, false);
        });
    }

    @Test
    public void testGetSendStream() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.getSendStream();
        });
    }

    @Test
    public void testGetSendWriter() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.getSendWriter();
        });
    }

    @Test
    public void testSendObject() throws IOException, EncodeException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.sendObject(null);
        });
    }
}
