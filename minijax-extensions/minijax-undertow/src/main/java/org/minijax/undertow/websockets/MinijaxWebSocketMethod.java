package org.minijax.undertow.websockets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class MinijaxWebSocketMethod {
    private final Object instance;
    private final Method method;

    public MinijaxWebSocketMethod(final Object instance, final Method method) {
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
            method.invoke(instance, params);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
