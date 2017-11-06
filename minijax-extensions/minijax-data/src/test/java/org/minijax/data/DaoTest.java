package org.minijax.data;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;
import org.minijax.MinijaxProperties;
import org.minijax.entity.test.Widget;

public class DaoTest {

    public static EntityManagerFactory getNewEntityManagerFactory() {
        final Map<String, String> props = new HashMap<>();
        props.put(MinijaxProperties.PERSISTENCE_UNIT_NAME, "testdb");
        props.put(MinijaxProperties.DB_DRIVER, "org.h2.jdbcx.JdbcDataSource");
        props.put(MinijaxProperties.DB_URL, "jdbc:h2:mem:");
        props.put(MinijaxProperties.DB_USERNAME, "");
        props.put(MinijaxProperties.DB_PASSWORD, "");
        props.put(SCHEMA_GENERATION_DATABASE_ACTION, "drop-and-create");
        return Persistence.createEntityManagerFactory("testdb", props);
    }

    /**
     * Dao wrapping in-memory H2 database.
     */
    public static class Dao extends BaseDao {
        private final EntityManagerFactory emf;

        public Dao(final EntityManagerFactory emf) {
            this.emf = emf;
            em = emf.createEntityManager();
        }

        public Dao() {
            this(getNewEntityManagerFactory());
        }

        @Override
        public void close() {
            super.close();
            emf.close();
        }
    }

    @Test
    public void testEntityCrud() throws Exception {
        try (final Dao dao = new Dao()) {
            // Create
            final Widget w1 = new Widget();
            w1.setName("My Widget");
            w1.generateHandle();
            dao.create(w1);
            assertNotNull(w1.getId());
            assertNotNull(w1.getCreatedDateTime());
            assertNotNull(w1.getUpdatedDateTime());
            assertEquals(w1.getCreatedDateTime(), w1.getUpdatedDateTime());

            // Read
            final Widget w2 = dao.read(Widget.class, w1.getId());
            assertNotNull(w2);
            assertEquals(w1.getId(), w2.getId());

            // Update
            final Widget w3 = dao.update(w2);
            assertNotNull(w3);
            assertNotEquals(w2.getCreatedDateTime(), w3.getUpdatedDateTime());

            // Delete (soft)
            dao.delete(w3);
            assertNotNull(w3.getDeletedDateTime());

            // Delete (hard)
            dao.purge(w3);
            assertNull(dao.read(Widget.class, w1.getId()));
        }
    }


    @Test
    public void testCreateConflict() {
        try (final Dao dao = new Dao()) {
            final Widget w1 = new Widget();
            w1.setName("First Widget");
            w1.setHandle("firsthandle");
            dao.create(w1);

            final Widget w2 = new Widget();
            w2.setName("Second Widget");
            w2.setHandle("firsthandle");
            dao.create(w2);

            fail("Expected ConflictException");

        } catch (final ConflictException ex) {
            assertEquals("handle", ex.getKey());
            assertEquals("firsthandle", ex.getValue());
        }
    }


    @Test
    public void testUpdateConflict() {
        try (final Dao dao = new Dao()) {
            final Widget w1 = new Widget();
            w1.setName("First Widget");
            w1.setHandle("firsthandle");
            dao.create(w1);

            final Widget w2 = new Widget();
            w2.setName("Second Widget");
            w2.setHandle("secondhandle");
            dao.create(w2);

            w2.setHandle("firsthandle");
            dao.update(w2);

            fail("Expected ConflictException");

        } catch (final ConflictException ex) {
            assertEquals("handle", ex.getKey());
            assertEquals("firsthandle", ex.getValue());
        }
    }


    @Test
    public void testReadByHandle() {
        try (final Dao dao = new Dao()) {
            final Widget w1 = new Widget();
            w1.setName("First Widget");
            w1.setHandle("firsthandle");
            dao.create(w1);

            final Widget w2 = dao.readByHandle(Widget.class, "firsthandle");
            assertNotNull(w2);
            assertEquals(w1, w2);
        }
    }


    @Test
    public void testReadByHandleNotFound() {
        try (final Dao dao = new Dao()) {
            final Widget w2 = dao.readByHandle(Widget.class, "notfound");
            assertNull(w2);
        }
    }


    @Test
    public void testReadPage() {
        try (final Dao dao = new Dao()) {
            final Widget w1 = new Widget();
            w1.setName("First Widget");
            w1.setHandle("firsthandle");
            dao.create(w1);

            final Widget w2 = new Widget();
            w2.setName("Second Widget");
            w2.setHandle("secondhandle");
            dao.create(w2);

            final long count = dao.countAll(Widget.class);
            assertEquals(2, count);

            final List<Widget> widgets = dao.readPage(Widget.class, 0, 100);
            assertEquals(2, widgets.size());
        }
    }
}
