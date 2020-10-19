package org.minijax.jndi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NotContextException;

public class MinijaxContext implements javax.naming.Context {
    private final Properties env;
    private final MinijaxNameParser nameParser;
    private final Map<Name, Object> namesToObjects;
    private final Map<Name, MinijaxContext> subContexts;

    public MinijaxContext(final Map<Object, Object> env, final boolean root) throws NamingException {
        this.env = new Properties();
        this.env.putAll(env);
        nameParser = new MinijaxNameParser(this);
        namesToObjects = Collections.synchronizedMap(new HashMap<>());
        subContexts = Collections.synchronizedMap(new HashMap<>());

        if (root) {
            createSubcontext("java:comp");
            createSubcontext("java:comp/env");
            createSubcontext("java:comp/env/ejb");
        }
    }

    private MinijaxContext(final MinijaxContext other) throws NamingException {
        this(other.env, false);
    }

    @Override
    public Hashtable<?, ?> getEnvironment() throws NamingException {
        return env;
    }

    @Override
    public MinijaxContext createSubcontext(final Name name) throws NamingException {
        if (name.size() > 1) {
            if (!subContexts.containsKey(name.getPrefix(1))) {
                throw new NameNotFoundException("Subcontext '" + name.getPrefix(1) + "' not found");
            }
            return subContexts.get(name.getPrefix(1)).createSubcontext(name.getSuffix(1));
        }

        final MinijaxContext newContext = new MinijaxContext(this.env, false);
        bind(name, newContext);
        return newContext;
    }

    @Override
    public Context createSubcontext(final String name) throws NamingException {
        return createSubcontext(nameParser.parse(name));
    }

    @Override
    public void destroySubcontext(final Name name) throws NamingException {
        if (name.size() > 1) {
            if (subContexts.containsKey(name.getPrefix(1))) {
                final Context subContext = subContexts.get(name.getPrefix(1));
                destroySubcontexts(subContext);
                return;
            }
            throw new NameNotFoundException();
        }

        if (namesToObjects.containsKey(name)) {
            throw new NotContextException();
        }

        if (!subContexts.containsKey(name)) {
            throw new NameNotFoundException();
        }

        final Context subContext = subContexts.get(name);
        destroySubcontexts(subContext);
        subContext.close();
        subContexts.remove(name);
    }

    private void destroySubcontexts(final Context context) throws NamingException {
        final NamingEnumeration<Binding> bindings = context.listBindings("");
        while (bindings.hasMore()) {
            final Binding binding = bindings.next();
            final String name = binding.getName();
            if (binding.getObject() instanceof Context) {
                final Context subContext = (Context) binding.getObject();
                destroySubcontexts(subContext);
                context.destroySubcontext(name);
            } else {
                context.unbind(name);
            }
        }
    }

    @Override
    public void destroySubcontext(final String name) throws NamingException {
        destroySubcontext(nameParser.parse(name));
    }

    @Override
    public Object lookup(final Name name) throws NamingException {
        if (name.size() == 0) {
            return new MinijaxContext(this);
        }

        final Name objName = name.getPrefix(1);

        if (name.size() > 1) {
            if (subContexts.containsKey(objName)) {
                return subContexts.get(objName).lookup(name.getSuffix(1));
            }
            throw new NamingException("Invalid subcontext '" + name + "' in context '" + objName + "'");
        }

        if (namesToObjects.containsKey(name)) {
            return namesToObjects.get(objName);
        }

        if (subContexts.containsKey(name)) {
            return subContexts.get(name);
        }

        throw new NameNotFoundException(name.toString());
    }

    @Override
    public Object lookup(final String name) throws NamingException {
        return lookup(nameParser.parse(name));
    }

    @Override
    public void bind(final Name name, final Object obj) throws NamingException {
        if (name.size() == 0) {
            throw new InvalidNameException("Cannot bind to an empty name.");
        }

        if (name.size() > 1) {
            final Name prefix = name.getPrefix(1);
            if (subContexts.containsKey(prefix)) {
                subContexts.get(prefix).bind(name.getSuffix(1), obj);
            } else {
                throw new NameNotFoundException(prefix.toString());
            }
        }

        if (namesToObjects.containsKey(name) || subContexts.containsKey(name)) {
            throw new NameAlreadyBoundException("Name " + name + " already bound.  Use rebind() to override");
        }

        if (obj instanceof MinijaxContext) {
            subContexts.put(name, (MinijaxContext) obj);
        } else {
            namesToObjects.put(name, obj);
        }
    }

    @Override
    public void bind(final String name, final Object obj) throws NamingException {
        bind(nameParser.parse(name), obj);
    }

    @Override
    public void unbind(final Name name) throws NamingException {
        if (name.isEmpty()) {
            throw new InvalidNameException("Cannot unbind to empty name");
        }

        if (name.size() == 1) {
            namesToObjects.remove(name);
            subContexts.remove(name);
            return;
        }

        final Object targetContext = lookup(name.getPrefix(name.size() - 1));
        if (!(targetContext instanceof Context)) {
            throw new NamingException("Cannot unbind object.");
        }

        ((Context) targetContext).unbind(name.getSuffix(name.size() - 1));
    }

    @Override
    public void unbind(final String name) throws NamingException {
        unbind(nameParser.parse(name));
    }

    @Override
    public void rebind(final Name name, final Object obj) throws NamingException {
        if (name.isEmpty()) {
            throw new InvalidNameException("Cannot bind to empty name");
        }

        final Object targetContext = lookup(name.getPrefix(name.size() - 1));
        if (!(targetContext instanceof Context)) {
            throw new NamingException("Cannot bind object.  Target context does not exist.");
        }

        unbind(name);
        bind(name, obj);
    }

    @Override
    public void rebind(final String name, final Object obj) throws NamingException {
        rebind(nameParser.parse(name), obj);
    }

    @Override
    public void rename(final Name oldName, final Name newName) throws NamingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void rename(final String oldName, final String newName) throws NamingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public NamingEnumeration<NameClassPair> list(final Name name) throws NamingException {
        if (name == null || name.isEmpty()) {
            final Map<Object, Object> enumStore = new HashMap<>();
            enumStore.putAll(namesToObjects);
            enumStore.putAll(subContexts);
            return new MinijaxNameClassPairEnumeration(enumStore);
        }

        final Name subName = name.getPrefix(1);
        if (namesToObjects.containsKey(subName)) {
            throw new NotContextException(name + " cannot be listed");
        }

        if (subContexts.containsKey(subName)) {
            return subContexts.get(subName).list(name.getSuffix(1));
        }

        throw new NamingException();
    }

    @Override
    public NamingEnumeration<NameClassPair> list(final String name) throws NamingException {
        return list(nameParser.parse(name));
    }

    @Override
    public NamingEnumeration<Binding> listBindings(final Name name) throws NamingException {
        if (name == null || name.isEmpty()) {
            final Map<Object, Object> enumStore = new HashMap<>();
            enumStore.putAll(namesToObjects);
            enumStore.putAll(subContexts);
            return new MinijaxBindingsEnumeration(enumStore);
        }

        final Name subName = name.getPrefix(1);
        if (subContexts.containsKey(subName)) {
            return subContexts.get(subName).listBindings(name.getSuffix(1));
        }

        throw new NamingException("AbstractContext#listBindings(\"" + name + "\"): subcontext not found.");
    }

    @Override
    public NamingEnumeration<Binding> listBindings(final String name) throws NamingException {
        return listBindings(nameParser.parse(name));
    }

    @Override
    public void close() throws NamingException {
        // Nothing to do
    }

    /*
     * Unsupported
     */

    @Override
    public Object lookupLink(final Name name) throws NamingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object lookupLink(final String name) throws NamingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public NameParser getNameParser(final Name name) throws NamingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public NameParser getNameParser(final String name) throws NamingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Name composeName(final Name name, final Name prefix) throws NamingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String composeName(final String name, final String prefix) throws NamingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object addToEnvironment(final String propName, final Object propVal) throws NamingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object removeFromEnvironment(final String propName) throws NamingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getNameInNamespace() throws NamingException {
        throw new UnsupportedOperationException();
    }
}
