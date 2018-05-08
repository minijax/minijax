package org.minijax.undertow.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.minijax.MinijaxApplication;

import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.spi.WebSocketHttpExchange;

public class MinijaxUndertowWebSocketConnectionCallback implements WebSocketConnectionCallback {
    private final MinijaxApplication application;
    private final Class<?> endpointClass;

    public MinijaxUndertowWebSocketConnectionCallback(
            final MinijaxApplication application,
            final Class<?> endpointClass) {
        this.application = application;
        this.endpointClass = endpointClass;
    }

    @Override
    public void onConnect(final WebSocketHttpExchange exchange, final WebSocketChannel channel) {
        try (final MinijaxUndertowWebSocketRequestContext ctx = new MinijaxUndertowWebSocketRequestContext(application, exchange)) {
            final MinijaxUndertowWebSocketBasicRemote basicRemote = new MinijaxUndertowWebSocketBasicRemote(channel);

            final Map<Class<?>, Object> params = new HashMap<>();
            params.put(javax.websocket.Session.class, new MinijaxUndertowWebSocketSession(basicRemote));

            final MinijaxUndertowWebSocketAdapter endpoint = new MinijaxUndertowWebSocketAdapter(endpointClass);
            endpoint.onOpen(params);

            channel.getReceiveSetter().set(new MinijaxUndertowWebSocketListener(application, endpoint, exchange));
            channel.resumeReceives();

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
