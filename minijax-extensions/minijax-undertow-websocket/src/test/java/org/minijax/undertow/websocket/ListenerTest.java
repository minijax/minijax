package org.minijax.undertow.websocket;

import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.websocket.OnMessage;

import org.junit.Test;
import org.minijax.Minijax;
import org.minijax.MinijaxApplicationContext;
import org.minijax.MinijaxRequestContext;
import org.minijax.test.MinijaxTest;

import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.spi.WebSocketHttpExchange;

public class ListenerTest extends MinijaxTest {

    public static class TestSocket {
        @OnMessage
        public String foo(final String msg) {
            return msg;
        }
    }

    @Test
    public void testHappyPath() throws IOException {
        final Minijax minijax = new Minijax().register(TestSocket.class);
        final MinijaxApplicationContext application = minijax.getDefaultApplication();

        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final MinijaxUndertowWebSocketAdapter adapter = new MinijaxUndertowWebSocketAdapter(ctx, TestSocket.class);
            final WebSocketHttpExchange exchange = mock(WebSocketHttpExchange.class);
            final MinijaxUndertowWebSocketListener listener = new MinijaxUndertowWebSocketListener(application, adapter, exchange);
            final WebSocketChannel channel = mock(WebSocketChannel.class);
            final BufferedTextMessage message = mock(BufferedTextMessage.class);
            listener.onFullTextMessage(channel, message);
        }
    }
}
