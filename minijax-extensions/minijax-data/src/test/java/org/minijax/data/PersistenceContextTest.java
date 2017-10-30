package org.minijax.data;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.minijax.Minijax;
import org.minijax.MinijaxProperties;
import org.minijax.MinijaxRequestContext;
import org.minijax.entity.test.Widget;
import org.minijax.test.MockHttpServletRequest;

public class PersistenceContextTest {

    public static class PersistenceContextDao {

        @PersistenceContext
        private EntityManager em;

        public Widget create(final Widget widget) {
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
                .property(MinijaxProperties.PERSISTENCE_UNIT_NAME, "testdb")
                .property(MinijaxProperties.DB_DRIVER, "org.h2.jdbcx.JdbcDataSource")
                .property(MinijaxProperties.DB_URL, "jdbc:h2:mem:persistenceContextTest")
                .property(MinijaxProperties.DB_USERNAME, "")
                .property(MinijaxProperties.DB_PASSWORD, "")
                .property(SCHEMA_GENERATION_DATABASE_ACTION, "drop-and-create")
                .register(PersistenceContextDao.class);

        final MockHttpServletRequest request = new MockHttpServletRequest("GET", URI.create("/"));

        try (MinijaxRequestContext context = new MinijaxRequestContext(container, request, null)) {
            final PersistenceContextDao dao = container.get(PersistenceContextDao.class);

            final Widget widget = new Widget();
            widget.setName("test");

            final Widget result = dao.create(widget);
            assertNotNull(result);
            assertNotNull(result.getId());
        }
    }
}
