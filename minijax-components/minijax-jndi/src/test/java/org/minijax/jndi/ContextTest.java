package org.minijax.jndi;

import static org.junit.Assert.*;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NotContextException;

import org.junit.Before;
import org.junit.Test;
import org.minijax.jndi.testmodel.DummyBean;

public class ContextTest {
    private Context ctx;

    @Before
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

    @Test(expected = NamingException.class)
    public void testLookupNotFound() throws NamingException {
        final Object result = ctx.lookup("bar");
        assertNotNull(result);
        assertEquals(MinijaxContext.class, result.getClass());
    }

    @Test(expected = NamingException.class)
    public void testLookupSubcontextNotFound() throws NamingException {
        final Object result = ctx.lookup("foo/bar");
        assertNotNull(result);
        assertEquals(MinijaxContext.class, result.getClass());
    }

    @Test(expected = NamingException.class)
    public void testBindEmptyString() throws NamingException {
        ctx.bind("", new DummyBean());
    }

    @Test
    public void testCreateSubcontext() throws NamingException {
        final Context result = ctx.createSubcontext("foo");
        assertNotNull(result);
        assertEquals(MinijaxContext.class, result.getClass());
    }

    @Test(expected = NameNotFoundException.class)
    public void testCreateSubcontextNotExists() throws NamingException {
        ctx.createSubcontext("foo/bar");
    }

    @Test
    public void testDestroySubcontext() throws NamingException {
        final Context c1 = ctx.createSubcontext("foo");
        assertNotNull(c1);

        final Context c2 = ctx.createSubcontext("foo/bar");
        assertNotNull(c2);

        ctx.destroySubcontext("foo/bar");
    }

    @Test(expected = NameNotFoundException.class)
    public void testDestroySubcontextNotFound() throws NamingException {
        ctx.destroySubcontext("notfound");
    }

    @Test(expected = NameNotFoundException.class)
    public void testDestroySubcontextNestedNotFound() throws NamingException {
        ctx.destroySubcontext("foo/notfound");
    }

    @Test(expected = NotContextException.class)
    public void testDestroySubcontextObject() throws NamingException {
        ctx.bind("foo", new DummyBean());
        ctx.destroySubcontext("foo");
    }

    @Test(expected = NamingException.class)
    public void testUnbind() throws NamingException {
        ctx.bind("foo", new DummyBean());
        ctx.unbind("foo");
        ctx.lookup("foo");
    }

    @Test(expected = NamingException.class)
    public void testUnbindContext() throws NamingException {
        ctx.createSubcontext("foo");
        ctx.bind("foo/bar", new DummyBean());
        ctx.unbind("foo/bar");
        ctx.lookup("foo/bar");
    }

    @Test(expected = NamingException.class)
    public void testUnbindEmptyString() throws NamingException {
        ctx.unbind("");
    }
}
