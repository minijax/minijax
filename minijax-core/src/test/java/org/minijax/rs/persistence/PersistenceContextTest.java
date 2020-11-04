package org.minijax.rs.persistence;

import static jakarta.ws.rs.HttpMethod.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;
import org.minijax.rs.MinijaxApplicationContext;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.test.MinijaxTestRequestContext;

class PersistenceContextTest {

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
    void testPersistenceContextInject() throws IOException {
        final Minijax container = new Minijax()
                .register(PersistenceFeature.class)
                .register(PersistenceContextDao.class);

        final MinijaxApplicationContext application = container.getDefaultApplication();

        try (MinijaxRequestContext context = new MinijaxTestRequestContext(application, GET, "/")) {
            final PersistenceContextDao dao = context.getResource(PersistenceContextDao.class);

            final Widget widget = new Widget();
            widget.setId(123);
            widget.setName("test");

            final Widget result = dao.create(widget);
            assertNotNull(result);
            assertEquals(123, result.getId());
        }

        container.getInjector().close();
    }
}
