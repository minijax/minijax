package org.minijax.undertow.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.minijax.MinijaxApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.spi.WebSocketHttpExchange;

public class MinijaxUndertowWebSocketListener extends AbstractReceiveListener {
    private static final Logger LOG = LoggerFactory.getLogger(MinijaxUndertowWebSocketListener.class);
    private final MinijaxApplication application;
    private final MinijaxUndertowWebSocketAdapter endpoint;
    private final WebSocketHttpExchange exchange;

    public MinijaxUndertowWebSocketListener(
            final MinijaxApplication application,
            final MinijaxUndertowWebSocketAdapter endpoint,
            final WebSocketHttpExchange exchange) {
        this.application = application;
        this.endpoint = endpoint;
        this.exchange = exchange;
    }

    @Override
    protected void onFullTextMessage(final WebSocketChannel channel, final BufferedTextMessage message) {
        try (final MinijaxUndertowWebSocketRequestContext ctx = new MinijaxUndertowWebSocketRequestContext(application, exchange)) {
            final MinijaxUndertowWebSocketBasicRemote basicRemote = new MinijaxUndertowWebSocketBasicRemote(channel);

            final Map<Class<?>, Object> params = new HashMap<>();
            params.put(javax.websocket.Session.class, new MinijaxUndertowWebSocketSession(basicRemote));
            params.put(String.class, message.getData());

            endpoint.onMessage(params);

        } catch (final IOException ex) {
            LOG.warn("Exception handling websocket message: {}", ex.getMessage(), ex);
        }
    }
}
