package org.minijax.persistence.criteria;

import static org.junit.Assert.*;

import javax.persistence.Tuple;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxEntityManagerFactory;
import org.minijax.persistence.MinijaxPersistenceProvider;
import org.minijax.persistence.testmodel.Widget;

public class MinijaxCriteriaBuilderTest {
    private MinijaxEntityManagerFactory emf;
    private MinijaxEntityManager em;
    private MinijaxCriteriaBuilder cb;

    @Before
    public void setUp() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        emf = provider.createEntityManagerFactory("testdb", null);
        em = emf.createEntityManager();
        cb = em.getCriteriaBuilder();
    }

    @After
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void testCreateClassQuery() {
        final MinijaxCriteriaQuery<Widget> q = cb.createQuery(Widget.class);
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);
        assertEquals(Widget.class, q.getResultType());
    }

    @Test
    public void testCreateObjectQuery() {
        final MinijaxCriteriaQuery<Object> q = cb.createQuery();
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);
        assertEquals(Object.class, q.getResultType());
    }

    @Test
    public void testCreateTupleQuery() {
        final MinijaxCriteriaQuery<Tuple> q = cb.createTupleQuery();
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(cb.tuple(w.get("id"), w.get("name")));
        assertEquals(Tuple.class, q.getSelection().getJavaType());
        assertEquals(Tuple.class, q.getResultType());
        assertTrue(q.getSelection().isCompoundSelection());
    }
}
