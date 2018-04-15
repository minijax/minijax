package org.minijax.undertow.websockets;

import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;

public class MinijaxWebSocketListener extends AbstractReceiveListener {
    private final MinijaxWebSocketAdapter endpoint;

    public MinijaxWebSocketListener(final MinijaxWebSocketAdapter endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    protected void onFullTextMessage(final WebSocketChannel channel, final BufferedTextMessage message) {
        //WebSockets.sendText(message.getData(), channel, null);
        endpoint.onMessage(message.getData());
    }
}
