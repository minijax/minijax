package org.minijax.jndi;

import java.util.Properties;

import javax.naming.CompoundName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

public class MinijaxNameParser implements NameParser {
    private final MinijaxContext context;

    public MinijaxNameParser(final MinijaxContext context) {
        this.context = context;
    }

    @Override
    public Name parse(final String name) throws NamingException {
        return new CompoundName(name != null ? name : "", (Properties) context.getEnvironment());
    }
}
