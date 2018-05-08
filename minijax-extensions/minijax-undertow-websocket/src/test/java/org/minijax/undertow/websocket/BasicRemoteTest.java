package org.minijax.undertow.websocket;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.websocket.EncodeException;

import org.junit.Test;

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

    @Test(expected = UnsupportedOperationException.class)
    public void testSetBatchingAllowed() throws IOException {
        final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
        remote.setBatchingAllowed(false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetBatchingAllowed() {
        final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
        remote.getBatchingAllowed();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFlushBatch() throws IOException {
        final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
        remote.flushBatch();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSendPing() throws IOException {
        final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
        remote.sendPing(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSendPong() throws IOException {
        final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
        remote.sendPong(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSendBinaryByteBuffer() throws IOException {
        final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
        remote.sendBinary(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSendTextStringBoolean() throws IOException {
        final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
        remote.sendText(null, false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSendBinaryByteBufferBoolean() throws IOException {
        final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
        remote.sendBinary(null, false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSendStream() throws IOException {
        final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
        remote.getSendStream();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSendWriter() throws IOException {
        final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
        remote.getSendWriter();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSendObject() throws IOException, EncodeException {
        final MinijaxUndertowWebSocketBasicRemote remote = new MinijaxUndertowWebSocketBasicRemote(null);
        remote.sendObject(null);
    }
}
