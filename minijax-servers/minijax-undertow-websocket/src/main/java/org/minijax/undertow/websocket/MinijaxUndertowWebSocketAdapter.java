package org.minijax.undertow.websocket;

import java.lang.reflect.Method;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;

import org.minijax.rs.MinijaxRequestContext;

public class MinijaxUndertowWebSocketAdapter {
    private final Object endpoint;
    private final MinijaxUndertowWebSocketMethod openMethod;
    private final MinijaxUndertowWebSocketMethod closeMethod;
    private final MinijaxUndertowWebSocketMethod messageMethod;
    private final MinijaxUndertowWebSocketMethod errorMethod;

    public MinijaxUndertowWebSocketAdapter(final MinijaxRequestContext context, final Class<?> endpointClass) {
        endpoint = context.get(endpointClass);

        MinijaxUndertowWebSocketMethod open = null;
        MinijaxUndertowWebSocketMethod close = null;
        MinijaxUndertowWebSocketMethod message = null;
        MinijaxUndertowWebSocketMethod error = null;

        for (final Method method : endpointClass.getDeclaredMethods()) {
            if (method.getAnnotation(OnOpen.class) != null) {
                open = new MinijaxUndertowWebSocketMethod(endpoint, method);
            }
            if (method.getAnnotation(OnClose.class) != null) {
                close = new MinijaxUndertowWebSocketMethod(endpoint, method);
            }
            if (method.getAnnotation(OnMessage.class) != null) {
                message = new MinijaxUndertowWebSocketMethod(endpoint, method);
            }
            if (method.getAnnotation(OnError.class) != null) {
                error = new MinijaxUndertowWebSocketMethod(endpoint, method);
            }
        }

        openMethod = open;
        closeMethod = close;
        messageMethod = message;
        errorMethod = error;
    }

    public void onOpen(final Map<Class<?>, Object> params) {
        if (openMethod != null) {
            openMethod.invoke(params);
        }
    }

    public void onClose(final Map<Class<?>, Object> params) {
        if (closeMethod != null) {
            closeMethod.invoke(params);
        }
    }

    public void onMessage(final Map<Class<?>, Object> params) {
        if (messageMethod != null) {
            messageMethod.invoke(params);
        }
    }

    public void onError(final Map<Class<?>, Object> params) {
        if (errorMethod != null) {
            errorMethod.invoke(params);
        }
    }
}
