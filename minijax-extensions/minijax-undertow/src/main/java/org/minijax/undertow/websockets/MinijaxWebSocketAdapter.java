package org.minijax.undertow.websockets;

import java.lang.reflect.Method;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;

import org.minijax.MinijaxRequestContext;

public class MinijaxWebSocketAdapter {
    private final Class<?> endpointClass;
    private final Object endpoint;
    private final MinijaxWebSocketMethod openMethod;
    private final MinijaxWebSocketMethod closeMethod;
    private final MinijaxWebSocketMethod messageMethod;
    private final MinijaxWebSocketMethod errorMethod;

    public MinijaxWebSocketAdapter(final Class<?> endpointClass) {
        this.endpointClass = endpointClass;
        endpoint = MinijaxRequestContext.getThreadLocal().get(endpointClass);

        MinijaxWebSocketMethod openMethod = null;
        MinijaxWebSocketMethod closeMethod = null;
        MinijaxWebSocketMethod messageMethod = null;
        MinijaxWebSocketMethod errorMethod = null;

        for (final Method method : endpointClass.getDeclaredMethods()) {
            if (method.getAnnotation(OnOpen.class) != null) {
                openMethod = new MinijaxWebSocketMethod(endpoint, method);
            }
            if (method.getAnnotation(OnClose.class) != null) {
                closeMethod = new MinijaxWebSocketMethod(endpoint, method);
            }
            if (method.getAnnotation(OnMessage.class) != null) {
                messageMethod = new MinijaxWebSocketMethod(endpoint, method);
            }
            if (method.getAnnotation(OnError.class) != null) {
                errorMethod = new MinijaxWebSocketMethod(endpoint, method);
            }
        }

        this.openMethod = openMethod;
        this.closeMethod = closeMethod;
        this.messageMethod = messageMethod;
        this.errorMethod = errorMethod;
    }

    public void onOpen() {
        if (openMethod != null) {
            openMethod.invoke(null);
        }
    }

    public void onClose() {
        if (closeMethod != null) {
            closeMethod.invoke(null);
        }
    }

    public void onMessage(final String text) {
        if (messageMethod != null) {
            messageMethod.invoke(null);
        }
    }

    public void onError(final Exception ex) {
        if (errorMethod != null) {
            errorMethod.invoke(null);
        }
    }
}
