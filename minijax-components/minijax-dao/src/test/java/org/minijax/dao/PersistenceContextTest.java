package org.minijax.dao;

import static javax.ws.rs.HttpMethod.*;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.minijax.Minijax;
import org.minijax.dao.PersistenceFeature;
import org.minijax.dao.test.Widget;
import org.minijax.rs.MinijaxApplicationContext;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.test.MinijaxTestRequestContext;

public class PersistenceContextTest {

    static class PersistenceContextDao {

        @PersistenceContext
        private EntityManager em;

        Widget create(final Widget widget) {
            em.getTransaction().begin();
            em.persist(widget);
            em.flush();
            em.getTransaction().commit();
            return widget;
        }
    }


    @Test
    public void testPersistenceContextInject() throws IOException {
        final Minijax container = new Minijax()
                .register(PersistenceFeature.class)
                .register(PersistenceContextDao.class);

        final MinijaxApplicationContext application = container.getDefaultApplication();

        try (MinijaxRequestContext context = new MinijaxTestRequestContext(application, GET, "/")) {
            final PersistenceContextDao dao = context.getResource(PersistenceContextDao.class);

            final Widget widget = new Widget();
            widget.setName("test");

            final Widget result = dao.create(widget);
            assertNotNull(result);
            assertNotNull(result.getId());
        }

        container.getInjector().close();
    }
}
