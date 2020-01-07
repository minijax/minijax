package org.minijax.dao;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.persistence.PersistenceFeature;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.test.MinijaxTest;

public class DaoTest extends MinijaxTest {
    private MinijaxRequestContext context;
    private Dao dao;

    static class Dao extends DefaultBaseDao {
    }

    @BeforeClass
    public static void setUpDaoTest() {
        getServer().register(PersistenceFeature.class);
    }

    @Before
    public void setUp() {
        context = createRequestContext();
        dao = context.getResource(Dao.class);
    }

    @After
    public void tearDown() throws IOException {
        context.close();
    }

    @Test
    public void testEntityCrud() throws Exception {
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
        w2.setHandle("newhandle"); // Must change a value for update to happen
        Thread.sleep(1L); // NOSONAR - Updated time should be after created time

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

    @Test
    public void testCreateConflict() {
        try {
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
        }
    }

    @Test
    public void testUpdateConflict() {
        try {
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
        }
    }

    @Test
    public void testReadByHandle() {
        final Widget w1 = new Widget();
        w1.setName("Unique Widget");
        w1.setHandle("uniquehandle");
        dao.create(w1);

        final Widget w2 = dao.readByHandle(Widget.class, "uniquehandle");
        assertNotNull(w2);
        assertEquals(w1, w2);
    }

    @Test
    public void testReadByHandleNotFound() {
        final Widget w2 = dao.readByHandle(Widget.class, "notfound");
        assertNull(w2);
    }

    @Test
    public void testReadPage() {
        // Delete any existing
        for (final Widget w : dao.readPage(Widget.class, 0, 100)) {
            dao.purge(w);
        }

        final Widget w1 = new Widget();
        w1.setName("First Widget");
        w1.setHandle("page1");
        dao.create(w1);

        final Widget w2 = new Widget();
        w2.setName("Second Widget");
        w2.setHandle("page2");
        dao.create(w2);

        final long count = dao.countAll(Widget.class);
        assertEquals(2, count);

        final List<Widget> widgets = dao.readPage(Widget.class, 0, 100);
        assertEquals(2, widgets.size());
    }
}
