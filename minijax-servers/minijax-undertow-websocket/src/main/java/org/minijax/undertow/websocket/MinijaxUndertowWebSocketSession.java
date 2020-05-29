package org.minijax.undertow.websocket;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.websocket.CloseReason;
import jakarta.websocket.Extension;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.MessageHandler.Partial;
import jakarta.websocket.MessageHandler.Whole;
import jakarta.websocket.RemoteEndpoint.Async;
import jakarta.websocket.RemoteEndpoint.Basic;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

public class MinijaxUndertowWebSocketSession implements Session {
    private final MinijaxUndertowWebSocketBasicRemote basicRemote;

    public MinijaxUndertowWebSocketSession(final MinijaxUndertowWebSocketBasicRemote basicRemote) {
        this.basicRemote = basicRemote;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Basic getBasicRemote() {
        return basicRemote;
    }

    @Override
    public void close() {
        // Nothing to do?
    }

    /*
     * Unsupported
     */

    @Override
    public WebSocketContainer getContainer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addMessageHandler(final MessageHandler handler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void addMessageHandler(final Class<T> clazz, final Whole<T> handler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void addMessageHandler(final Class<T> clazz, final Partial<T> handler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<MessageHandler> getMessageHandlers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeMessageHandler(final MessageHandler handler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getProtocolVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getNegotiatedSubprotocol() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Extension> getNegotiatedExtensions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSecure() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOpen() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMaxIdleTimeout() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxIdleTimeout(final long milliseconds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxBinaryMessageBufferSize(final int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaxBinaryMessageBufferSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxTextMessageBufferSize(final int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaxTextMessageBufferSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Async getAsyncRemote() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close(final CloseReason closeReason) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI getRequestURI() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, List<String>> getRequestParameterMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getQueryString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getPathParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getUserProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Session> getOpenSessions() {
        throw new UnsupportedOperationException();
    }
}
