package org.minijax.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaUpdate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.persistence.dialect.AnsiSqlBuilder;
import org.minijax.persistence.testmodel.KitchenSink;
import org.minijax.persistence.testmodel.User;
import org.minijax.persistence.testmodel.Widget;

public class MinijaxEntityManagerTest {
    private MinijaxEntityManagerFactory emf;
    private MinijaxEntityManager em;
    private Widget widget;
    private Map<String, Object> properties;
    private LockModeType lockMode;
    private FlushModeType flushMode;

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
    public void testCreateNamedQuery1() {
        em.createNamedQuery("");
    }

    @Test
    public void testCreateNamedQuery2() {
        final MinijaxQuery<User> query = em.createNamedQuery("User.findByName", User.class);
        query.setParameter("name", "Cody");

        final AnsiSqlBuilder<User> sqlBuilder = new AnsiSqlBuilder<>(em, query);
        sqlBuilder.buildSelect();

        assertEquals(
                "SELECT t0.ID, t0.NAME, t0.ADDRESS FROM USER t0 WHERE t0.NAME=?",
                sqlBuilder.getSql());
    }

    @Test
    public void testPersistAndFind() {
        final KitchenSink ks = new KitchenSink();
        ks.setId(UUID.randomUUID());
        ks.setCreatedDateTime(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        ks.setMyIntTimestamp(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        ks.setMyStringUuid(UUID.randomUUID());
        ks.setMyInt(123);
        ks.setMyString("foo bar");
        ks.setMyBytes(new byte[] { 1, 2, 3 });
        ks.setMyTimestamp(Timestamp.valueOf("2000-01-01 12:00:00"));

        em.getTransaction().begin();
        em.persist(ks);
        em.getTransaction().commit();

        final KitchenSink check = em.find(KitchenSink.class, ks.getId());
        assertNotNull(check);
        assertEquals(ks.getId(), check.getId());
        assertEquals(ks.getCreatedDateTime(), check.getCreatedDateTime());
        assertEquals(ks.getMyIntTimestamp(), check.getMyIntTimestamp());
        assertEquals(ks.getMyStringUuid(), check.getMyStringUuid());
    }

    /*
     * Unsupported
     */

    @Test
    public void testFind1() {
        assertThrows(UnsupportedOperationException.class, () -> em.find(Widget.class, "123", properties));
    }

    @Test
    public void testFind2() {
        assertThrows(UnsupportedOperationException.class, () -> em.find(Widget.class, "123", lockMode));
    }

    @Test
    public void testFind3() {
        assertThrows(UnsupportedOperationException.class, () -> em.find(Widget.class, "123", lockMode, properties));
    }

    @Test
    public void testGetReference() {
        assertThrows(UnsupportedOperationException.class, () -> em.getReference(Widget.class, "123"));
    }

    @Test
    public void testSetFlushMode() {
        assertThrows(UnsupportedOperationException.class, () -> em.setFlushMode(flushMode));
    }

    @Test
    public void testGetFlushMode() {
        assertThrows(UnsupportedOperationException.class, () -> em.getFlushMode());
    }

    @Test
    public void testLock1() {
        assertThrows(UnsupportedOperationException.class, () -> em.lock(widget, lockMode));
    }

    @Test
    public void testLock2() {
        assertThrows(UnsupportedOperationException.class, () -> em.lock(widget, lockMode, properties));
    }

    @Test
    public void testRefresh1() {
        assertThrows(UnsupportedOperationException.class, () -> em.refresh(widget));
    }

    @Test
    public void testRefresh2() {
        assertThrows(UnsupportedOperationException.class, () -> em.refresh(widget, properties));
    }

    @Test
    public void testRefresh3() {
        assertThrows(UnsupportedOperationException.class, () -> em.refresh(widget, lockMode));
    }

    @Test
    public void testRefresh4() {
        assertThrows(UnsupportedOperationException.class, () -> em.refresh(widget, lockMode, properties));
    }

    @Test
    public void testClear() {
        assertThrows(UnsupportedOperationException.class, () -> em.clear());
    }

    @Test
    public void testDetach() {
        assertThrows(UnsupportedOperationException.class, () -> em.detach(widget));
    }

    @Test
    public void testContains() {
        assertThrows(UnsupportedOperationException.class, () -> em.contains(widget));
    }

    @Test
    public void testGetLockMode() {
        assertThrows(UnsupportedOperationException.class, () -> em.getLockMode(widget));
    }

    @Test
    public void testSetProperty() {
        assertThrows(UnsupportedOperationException.class, () -> em.setProperty("foo", "bar"));
    }

    @Test
    public void testGetProperties() {
        assertThrows(UnsupportedOperationException.class, () -> em.getProperties());
    }

    @Test
    public void testCreateQuery1() {
        assertThrows(UnsupportedOperationException.class, () -> em.createQuery(""));
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testCreateQuery2() {
        assertThrows(UnsupportedOperationException.class, () -> em.createQuery((CriteriaUpdate) null));
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testCreateQuery3() {
        assertThrows(UnsupportedOperationException.class, () -> em.createQuery((CriteriaDelete) null));
    }

    @Test
    public void testCreateNativeQuery3() {
        assertThrows(UnsupportedOperationException.class, () -> em.createNativeQuery(""));
    }

    @Test
    public void testCreateNativeQuery1() {
        assertThrows(UnsupportedOperationException.class, () -> em.createNativeQuery("", Widget.class));
    }

    @Test
    public void testCreateNativeQuery2() {
        assertThrows(UnsupportedOperationException.class, () -> em.createNativeQuery("", ""));
    }

    @Test
    public void testCreateNamedStoredProcedureQuery() {
        assertThrows(UnsupportedOperationException.class, () -> em.createNamedStoredProcedureQuery(""));
    }

    @Test
    public void testCreateStoredProcedureQuery1() {
        assertThrows(UnsupportedOperationException.class, () -> em.createStoredProcedureQuery(""));
    }

    @Test
    public void testCreateStoredProcedureQuery2() {
        assertThrows(UnsupportedOperationException.class, () -> em.createStoredProcedureQuery("", Widget.class));
    }

    @Test
    public void testCreateStoredProcedureQuery3() {
        assertThrows(UnsupportedOperationException.class, () -> em.createStoredProcedureQuery("", "", ""));
    }

    @Test
    public void testJoinTransaction() {
        assertThrows(UnsupportedOperationException.class, () -> em.joinTransaction());
    }

    @Test
    public void testIsJoinedToTransaction() {
        assertThrows(UnsupportedOperationException.class, () -> em.isJoinedToTransaction());
    }

    @Test
    public void testUnwrap() {
        assertThrows(UnsupportedOperationException.class, () -> em.unwrap(Widget.class));
    }

    @Test
    public void testGetDelegate() {
        assertThrows(UnsupportedOperationException.class, () -> em.getDelegate());
    }

    @Test
    public void testIsOpen() {
        assertThrows(UnsupportedOperationException.class, () -> em.isOpen());
    }

    @Test
    public void testCreateEntityGraph1() {
        assertThrows(UnsupportedOperationException.class, () -> em.createEntityGraph(Widget.class));
    }

    @Test
    public void testCreateEntityGraph2() {
        assertThrows(UnsupportedOperationException.class, () -> em.createEntityGraph(""));
    }

    @Test
    public void testGetEntityGraph() {
        assertThrows(UnsupportedOperationException.class, () -> em.getEntityGraph(""));
    }

    @Test
    public void testGetEntityGraphs() {
        assertThrows(UnsupportedOperationException.class, () -> em.getEntityGraphs(Widget.class));
    }
}
