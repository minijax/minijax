package org.minijax.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Parameter;
import jakarta.persistence.TemporalType;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.persistence.testmodel.Widget;

class MinijaxNativeQueryTest {
    private MinijaxEntityManagerFactory emf;
    private MinijaxEntityManager em;
    private MinijaxNativeQuery<Widget> query;

    @BeforeEach
    public void setUp() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        emf = provider.createEntityManagerFactory("testdb", null);
        em = emf.createEntityManager();
        query = new MinijaxNativeQuery<>(em, Widget.class, "SELECT *", Collections.emptyList());
    }

    @AfterEach
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    void testBasic() {
    }

    /*
     * Unsupported
     */

    @Test
    void testExecuteUpdate() {
        assertThrows(UnsupportedOperationException.class, () -> query.executeUpdate());
    }

    @Test
    void testGetMaxResults() {
        assertThrows(UnsupportedOperationException.class, () -> query.getMaxResults());
    }

    @Test
    void testGetFirstResult() {
        assertThrows(UnsupportedOperationException.class, () -> query.getFirstResult());
    }

    @Test
    void testGetHints() {
        assertThrows(UnsupportedOperationException.class, () -> query.getHints());
    }

    @Test
    void testGetParameters() {
        assertThrows(UnsupportedOperationException.class, () -> query.getParameters());
    }

    @Test
    void testGetParameter1() {
        assertThrows(UnsupportedOperationException.class, () -> query.getParameter(""));
    }

    @Test
    void testGetParameter2() {
        assertThrows(UnsupportedOperationException.class, () -> query.getParameter("", Widget.class));
    }

    @Test
    void testGetParameter3() {
        assertThrows(UnsupportedOperationException.class, () -> query.getParameter(0));
    }

    @Test
    void testGetParameter4() {
        assertThrows(UnsupportedOperationException.class, () -> query.getParameter(0, Widget.class));
    }

    @Test
    void testIsBound() {
        assertThrows(UnsupportedOperationException.class, () -> query.isBound(null));
    }

    @Test
    void testGetParameterValue1() {
        assertThrows(UnsupportedOperationException.class, () -> query.getParameterValue((Parameter<Object>) null));
    }

    @Test
    void testGetParameterValue2() {
        assertThrows(UnsupportedOperationException.class, () -> query.getParameterValue(""));
    }

    @Test
    void testGetParameterValue3() {
        assertThrows(UnsupportedOperationException.class, () -> query.getParameterValue(0));
    }

    @Test
    void testGetFlushMode() {
        assertThrows(UnsupportedOperationException.class, () -> query.getFlushMode());
    }

    @Test
    void testGetLockMode() {
        assertThrows(UnsupportedOperationException.class, () -> query.getLockMode());
    }

    @Test
    void testUnwrap() {
        assertThrows(UnsupportedOperationException.class, () -> query.unwrap(Widget.class));
    }

    @Test
    void testSetMaxResults() {
        assertThrows(UnsupportedOperationException.class, () -> query.setMaxResults(0));
    }

    @Test
    void testSetFirstResult() {
        assertThrows(UnsupportedOperationException.class, () -> query.setFirstResult(0));
    }

    @Test
    void testSetHint() {
        assertThrows(UnsupportedOperationException.class, () -> query.setHint("", null));
    }

    @Test
    void testSetParameter1() {
        assertThrows(UnsupportedOperationException.class, () -> query.setParameter((Parameter<Object>) null, null));
    }

    @Test
    void testSetParameter2() {
        assertThrows(UnsupportedOperationException.class, () -> query.setParameter((Parameter<Calendar>) null, null, TemporalType.DATE));
    }

    @Test
    void testSetParameter3() {
        assertThrows(UnsupportedOperationException.class, () -> query.setParameter((Parameter<Date>) null, null, TemporalType.DATE));
    }

    @Test
    void testSetParameter4() {
        assertThrows(UnsupportedOperationException.class, () -> query.setParameter("", null));
    }

    @Test
    void testSetParameter5() {
        assertThrows(UnsupportedOperationException.class, () -> query.setParameter("", (Calendar) null, TemporalType.DATE));
    }

    @Test
    void testSetParameter6() {
        assertThrows(UnsupportedOperationException.class, () -> query.setParameter("", (Date) null, TemporalType.DATE));
    }

    @Test
    void testSetParameter7() {
        assertThrows(UnsupportedOperationException.class, () -> query.setParameter(0, null));
    }

    @Test
    void testSetParameter8() {
        assertThrows(UnsupportedOperationException.class, () -> query.setParameter(0, (Calendar) null, TemporalType.DATE));
    }

    @Test
    void testSetParameter() {
        assertThrows(UnsupportedOperationException.class, () -> query.setParameter(0, (Date) null, TemporalType.DATE));
    }

    @Test
    void testSetFlushMode() {
        assertThrows(UnsupportedOperationException.class, () -> query.setFlushMode(FlushModeType.AUTO));
    }

    @Test
    void testSetLockMode() {
        assertThrows(UnsupportedOperationException.class, () -> query.setLockMode(LockModeType.NONE));
    }
}
