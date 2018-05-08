package org.minijax.undertow.websocket;

import java.lang.reflect.Method;
import java.util.Map;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinijaxUndertowWebSocketMethod {
    private static final Logger LOG = LoggerFactory.getLogger(MinijaxUndertowWebSocketMethod.class);
    private final Object instance;
    private final Method method;

    public MinijaxUndertowWebSocketMethod(final Object instance, final Method method) {
        this.instance = instance;
        this.method = method;
    }

    public void invoke(final Map<Class<?>, Object> args) {
        final Class<?>[] paramTypes = method.getParameterTypes();
        final Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            params[i] = args.get(paramTypes[i]);
        }
        try {
            final Object result = method.invoke(instance, params);
            if (result instanceof String) {
                ((Session) args.get(Session.class)).getBasicRemote().sendText((String) result);
            }

        } catch (final Exception ex) {
            LOG.warn("Exception invoking websocket handler: {}", ex.getMessage(), ex);
        }
    }
}
