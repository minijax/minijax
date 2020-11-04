package org.minijax.undertow.websocket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import jakarta.websocket.EncodeException;

import org.junit.jupiter.api.Test;

import io.undertow.websockets.core.StreamSinkFrameChannel;
import io.undertow.websockets.core.WebSocketChannel;

class BasicRemoteTest {

    @Test
    void testSendTextString() throws IOException {
        final StreamSinkFrameChannel sink = mock(StreamSinkFrameChannel.class);
        when(sink.send(any())).thenReturn(true);
        when(sink.flush()).thenReturn(true);

        final WebSocketChannel channel = mock(WebSocketChannel.class);
        when(channel.send(any())).thenReturn(sink);

        final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(channel);
        remote.sendText("test");
    }

    @Test
    void testSetBatchingAllowed() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.setBatchingAllowed(false);
        });
    }

    @Test
    void testGetBatchingAllowed() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.getBatchingAllowed();
        });
    }

    @Test
    void testFlushBatch() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.flushBatch();
        });
    }

    @Test
    void testSendPing() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.sendPing(null);
        });
    }

    @Test
    void testSendPong() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.sendPong(null);
        });
    }

    @Test
    void testSendBinaryByteBuffer() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.sendBinary(null);
        });
    }

    @Test
    void testSendTextStringBoolean() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.sendText(null, false);
        });
    }

    @Test
    void testSendBinaryByteBufferBoolean() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.sendBinary(null, false);
        });
    }

    @Test
    void testGetSendStream() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.getSendStream();
        });
    }

    @Test
    void testGetSendWriter() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.getSendWriter();
        });
    }

    @Test
    void testSendObject() throws IOException, EncodeException {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
            remote.sendObject(null);
        });
    }
}
