package org.minijax.persistence.dialect;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxEntityManagerFactory;
import org.minijax.persistence.MinijaxPersistenceProvider;
import org.minijax.persistence.testmodel.User;
import org.minijax.persistence.testmodel.Widget;

public class AnsiSqlDialectTest {
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
    public void testPersist() {
        final Widget widget = new Widget();
        widget.setId(123);
        widget.setName("foo");

        em.getTransaction().begin();
        em.persist(widget);
        em.getTransaction().commit();

        final Widget check = em.find(Widget.class, 123);
        assertNotNull(check);
        assertEquals(123, check.getId());
        assertEquals("foo", check.getName());
    }

    @Test
    @Ignore("needs updated LazySet query in MinijaxEntityType")
    public void testPersistAssociation() {
        final User u1 = new User();
        u1.setId(123);
        u1.setName("Alice");

        final User u2 = new User();
        u2.setId(456);
        u2.setName("Bob");

        em.getTransaction().begin();
        em.persist(u1);
        em.persist(u2);
        em.getTransaction().commit();

        u1.getFollowing().add(u2);

        em.getTransaction().begin();
        em.merge(u1);
        em.getTransaction().commit();

        final User check = em.find(User.class, 123);
        assertNotNull(check);
        assertEquals(1, check.getFollowing().size());
    }
}
