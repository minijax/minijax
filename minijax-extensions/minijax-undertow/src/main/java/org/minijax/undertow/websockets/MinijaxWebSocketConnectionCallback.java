package org.minijax.undertow.websockets;

import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.spi.WebSocketHttpExchange;

public class MinijaxWebSocketConnectionCallback implements WebSocketConnectionCallback {
    private final Class<?> endpointClass;

    public MinijaxWebSocketConnectionCallback(final Class<?> endpointClass) {
        this.endpointClass = endpointClass;
    }

    @Override
    public void onConnect(final WebSocketHttpExchange exchange, final WebSocketChannel channel) {
//        final Object endpoint = MinijaxRequestContext.getThreadLocal().get(endpointClass);
//
//        // TODO: Call endpoint method annotated with @OnConnect
//
//        for (final Method method : endpointClass.getDeclaredMethods()) {
//
//        }

        final MinijaxWebSocketAdapter endpoint = new MinijaxWebSocketAdapter(endpointClass);
        endpoint.onOpen();

        channel.getReceiveSetter().set(new MinijaxWebSocketListener(endpoint));
        channel.resumeReceives();
    }
}
