package org.minijax.websocket;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint("/echo")
public class WebSocketResource {
    private static final Logger log = LoggerFactory.getLogger(WebSocketResource.class);

    /**
     * This method is invoked when the client closes a WebSocket connection.
     *
     * @param session
     * @return
     */
    @OnClose
    public void onClose(final Session session) {
        log.info("[Session {}] Session has been closed.", session.getId());
    }

    /**
     * This method is invoked when an error was detected on the connection.
     *
     * @param session
     * @param t
     * @return
     */
    @OnError
    public void onError(final Session session, final Throwable t) {
        log.info("[Session {}] An error has been detected: {}.", session.getId(), t.getMessage());
    }

    /**
     * This method is invoked each time that the client receives a WebSocket
     * message.
     *
     * @param message
     * @param session
     * @return
     */
    @OnMessage
    public String onMessage(final String message, final Session session) {
        log.info("[Session {}] Sending message: {}", session.getId(), message);
        return message; // echo back the message received
    }

    /**
     * OnOpen (when a socket has been opened) allows us to intercept the
     * creation of a new session. The session class allows us to send data to
     * the user. In the method onOpen, we'll let the user know that the
     * handshake was successful.
     */
    @OnOpen
    public void onOpen(final Session session) {
        log.info("[Session {}] Session has been opened.", session.getId());
        try {
            session.getBasicRemote().sendText("Connection Established");
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}
