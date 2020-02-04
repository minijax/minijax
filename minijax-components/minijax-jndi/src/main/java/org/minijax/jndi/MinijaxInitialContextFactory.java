package org.minijax.jndi;

import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;

public class MinijaxInitialContextFactory implements javax.naming.spi.InitialContextFactory  {

    @Override
    @SuppressWarnings("unchecked")
    public Context getInitialContext(final Hashtable<?, ?> environment) throws NamingException {
        initEnv((Hashtable<Object, Object>) environment);
        return new MinijaxContext((Hashtable<Object, Object>) environment, true);
    }

    static void initEnv(final Map<Object, Object> env) {
        env.putIfAbsent("java.naming.factory.initial", "org.minijax.jndi.MinijaxInitialContextFactory");
        env.putIfAbsent("jndi.syntax.direction", "left_to_right");
        env.putIfAbsent("jndi.syntax.separator", "/");
    }
}
