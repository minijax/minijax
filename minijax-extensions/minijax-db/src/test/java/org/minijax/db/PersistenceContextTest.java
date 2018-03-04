package org.minijax.db;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.minijax.Minijax;
import org.minijax.MinijaxApplication;
import org.minijax.MinijaxRequestContext;
import org.minijax.db.test.Widget;
import org.minijax.test.MockHttpServletRequest;

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

        final MinijaxApplication application = container.getDefaultApplication();

        final MockHttpServletRequest request = new MockHttpServletRequest("GET", URI.create("/"));

        try (MinijaxRequestContext context = new MinijaxRequestContext(application, request, null)) {
            final PersistenceContextDao dao = container.getResource(PersistenceContextDao.class);

            final Widget widget = new Widget();
            widget.setName("test");

            final Widget result = dao.create(widget);
            assertNotNull(result);
            assertNotNull(result.getId());
        }

        container.getInjector().close();
    }
}
