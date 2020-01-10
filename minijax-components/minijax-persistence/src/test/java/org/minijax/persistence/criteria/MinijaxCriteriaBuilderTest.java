package org.minijax.persistence.criteria;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxEntityManagerFactory;
import org.minijax.persistence.MinijaxPersistenceProvider;
import org.minijax.persistence.MinijaxQuery;
import org.minijax.persistence.Widget;

public class MinijaxCriteriaBuilderTest {
    private MinijaxEntityManagerFactory emf;
    private MinijaxEntityManager em;

    @Before
    public void setUp() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        emf = provider.createEntityManagerFactory("testdb", null);
        em = emf.createEntityManager();
    }

    @After
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void testParseSelect() {
        final MinijaxCriteriaBuilder cb = em.getCriteriaBuilder();
        final MinijaxCriteriaQuery<Widget> q = cb.createQuery(Widget.class);
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);

        final MinijaxQuery<Widget> query = em.createQuery(q);
        assertNotNull(query);

        final List<Widget> results = query.getResultList();
        assertNotNull(results);
    }
}
