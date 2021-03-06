package org.minijax.undertow.websocket;

import static org.mockito.Mockito.*;

import jakarta.websocket.OnMessage;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;
import org.xnio.ChannelListener.Setter;

import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.spi.WebSocketHttpExchange;

class ConnectionCallbackTest {

    public static class TestSocket {
        @OnMessage
        public String foo(final String msg) {
            return msg;
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void testHappyPath() {
        final Minijax minijax = new Minijax().register(TestSocket.class);

        final MinijaxUndertowWebSocketConnectionCallback callback = new MinijaxUndertowWebSocketConnectionCallback(
                minijax.getDefaultApplication(),
                TestSocket.class);

        final WebSocketHttpExchange exchange = mock(WebSocketHttpExchange.class);

        final Setter<WebSocketChannel> receiverSetter = mock(Setter.class);

        final WebSocketChannel channel = mock(WebSocketChannel.class);
        when(channel.getReceiveSetter()).thenReturn(receiverSetter);

        callback.onConnect(exchange, channel);
    }
}
