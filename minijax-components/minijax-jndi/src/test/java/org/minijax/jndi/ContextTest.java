package org.minijax.jndi;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NotContextException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.jndi.testmodel.DummyBean;

public class ContextTest {
    private Context ctx;

    @BeforeEach
    public void setUp() throws NamingException {
        final Hashtable<String, String> props = new Hashtable<>();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.minijax.jndi.MinijaxInitialContextFactory");
        ctx = new InitialContext(props);
    }

    @Test
    public void testSimple() throws NamingException {
        ctx.bind("java:comp/env/ejb/myBean", new DummyBean());
        final DummyBean myBean = (DummyBean) ctx.lookup("java:comp/env/ejb/myBean");
        assertNotNull(myBean);
    }

    @Test
    public void testLookupEmptyString() throws NamingException {
        final Object result = ctx.lookup("");
        assertNotNull(result);
        assertEquals(MinijaxContext.class, result.getClass());
    }

    @Test
    public void testLookupSubcontext() throws NamingException {
        final Context c1 = ctx.createSubcontext("foo");
        assertNotNull(c1);

        final Object result = ctx.lookup("foo");
        assertNotNull(result);
        assertEquals(c1, result);
    }

    @Test
    public void testLookupNotFound() throws NamingException {
        assertThrows(NamingException.class, () -> ctx.lookup("bar"));
    }

    @Test
    public void testLookupSubcontextNotFound() throws NamingException {
        assertThrows(NamingException.class, () -> ctx.lookup("foo/bar"));
    }

    @Test
    public void testBindEmptyString() throws NamingException {
        assertThrows(NamingException.class, () -> ctx.bind("", new DummyBean()));
    }

    @Test
    public void testCreateSubcontext() throws NamingException {
        final Context result = ctx.createSubcontext("foo");
        assertNotNull(result);
        assertEquals(MinijaxContext.class, result.getClass());
    }

    @Test
    public void testCreateSubcontextNotExists() throws NamingException {
        assertThrows(NameNotFoundException.class, () -> ctx.createSubcontext("foo/bar"));
    }

    @Test
    public void testDestroySubcontext() throws NamingException {
        final Context c1 = ctx.createSubcontext("foo");
        assertNotNull(c1);

        final Context c2 = ctx.createSubcontext("foo/bar");
        assertNotNull(c2);

        ctx.destroySubcontext("foo/bar");
    }

    @Test
    public void testDestroySubcontextNotFound() throws NamingException {
        assertThrows(NameNotFoundException.class, () -> ctx.destroySubcontext("notfound"));
    }

    @Test
    public void testDestroySubcontextNestedNotFound() throws NamingException {
        assertThrows(NameNotFoundException.class, () -> ctx.destroySubcontext("foo/notfound"));
    }

    @Test
    public void testDestroySubcontextObject() throws NamingException {
        ctx.bind("foo", new DummyBean());
        assertThrows(NotContextException.class, () -> ctx.destroySubcontext("foo"));
    }

    @Test
    public void testUnbind() throws NamingException {
        ctx.bind("foo", new DummyBean());
        ctx.unbind("foo");
        assertThrows(NamingException.class, () -> ctx.lookup("foo"));
    }

    @Test
    public void testUnbindContext() throws NamingException {
        ctx.createSubcontext("foo");
        ctx.bind("foo/bar", new DummyBean());
        ctx.unbind("foo/bar");
        assertThrows(NamingException.class, () -> ctx.lookup("foo/bar"));
    }

    @Test
    public void testUnbindEmptyString() throws NamingException {
        assertThrows(NamingException.class, () -> ctx.unbind(""));
    }
}
