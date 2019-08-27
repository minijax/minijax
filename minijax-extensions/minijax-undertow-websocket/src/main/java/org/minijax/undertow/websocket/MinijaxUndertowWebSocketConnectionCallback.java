package org.minijax.undertow.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.StatusType;

import org.minijax.MinijaxApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;

public class MinijaxUndertowWebSocketConnectionCallback implements WebSocketConnectionCallback {
    private static final Logger LOG = LoggerFactory.getLogger(MinijaxUndertowWebSocketConnectionCallback.class);
    private final MinijaxApplicationContext application;
    private final Class<?> endpointClass;

    public MinijaxUndertowWebSocketConnectionCallback(
            final MinijaxApplicationContext application,
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

        } catch (final WebApplicationException ex) {
            LOG.debug("Web exception during websocket connection: {}", ex.getMessage(), ex);
            final StatusType status = ex.getResponse().getStatusInfo();
            WebSockets.sendClose(status.getStatusCode(), status.getReasonPhrase(), channel, null);

        } catch (final IOException ex) {
            LOG.warn("Exception during websocket connection: {}", ex.getMessage(), ex);
        }
    }
}
