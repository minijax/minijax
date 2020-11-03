package org.minijax.persistence.dialect;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxEntityManagerFactory;
import org.minijax.persistence.MinijaxPersistenceProvider;
import org.minijax.persistence.testmodel.Message;
import org.minijax.persistence.testmodel.User;
import org.minijax.persistence.testmodel.Widget;

public class AnsiSqlDialectTest {
    private MinijaxEntityManagerFactory emf;
    private MinijaxEntityManager em;

    @BeforeEach
    public void setUp() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        emf = provider.createEntityManagerFactory("testdb", null);
        em = emf.createEntityManager();
    }

    @AfterEach
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void testPersist() {
        final Widget widget = new Widget();
        widget.setId(123);
        widget.setName("persistTest");

        em.getTransaction().begin();
        em.persist(widget);
        em.getTransaction().commit();

        final Widget check = em.find(Widget.class, 123);
        assertNotNull(check);
        assertEquals(123, check.getId());
        assertEquals("persistTest", check.getName());

        em.getTransaction().begin();
        em.remove(widget);
        em.getTransaction().commit();

        final Widget check2 = em.find(Widget.class, 123);
        assertNull(check2);
    }

    @Test
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

    @Test
    public void testPersistReference() {
        final User u = new User();
        u.setId(111);
        u.setName("Alice");

        final Message m = new Message();
        m.setId(222);
        m.setAuthor(u);
        m.setText("Hello");

        em.getTransaction().begin();
        em.persist(u);
        em.persist(m);
        em.getTransaction().commit();

        final Message check = em.find(Message.class, 222);
        assertNotNull(check);
        assertEquals("Alice", check.getAuthor().getName());
    }
}
