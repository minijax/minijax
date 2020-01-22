package org.minijax.persistence;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxEntityManagerFactory;
import org.minijax.persistence.MinijaxNativeQuery;
import org.minijax.persistence.MinijaxPersistenceProvider;
import org.minijax.persistence.testmodel.Widget;

public class MinijaxNativeQueryTest {
    private MinijaxEntityManagerFactory emf;
    private MinijaxEntityManager em;
    private MinijaxNativeQuery<Widget> query;

    @Before
    public void setUp() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        emf = provider.createEntityManagerFactory("testdb", null);
        em = emf.createEntityManager();
        query = new MinijaxNativeQuery<>(em, Widget.class, "SELECT *", Collections.emptyList());
    }

    @After
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void testBasic() {
    }

    /*
     * Unsupported
     */

    @Test(expected = UnsupportedOperationException.class)
    public void testExecuteUpdate() {
        query.executeUpdate();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMaxResults() {
        query.getMaxResults();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetFirstResult() {
        query.getFirstResult();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetHints() {
        query.getHints();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetParameters() {
        query.getParameters();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetParameter1() {
        query.getParameter("");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetParameter2() {
        query.getParameter("", Widget.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetParameter3() {
        query.getParameter(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetParameter4() {
        query.getParameter(0, Widget.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsBound() {
        query.isBound(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetParameterValue1() {
        query.getParameterValue((Parameter<Object>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetParameterValue2() {
        query.getParameterValue("");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetParameterValue3() {
        query.getParameterValue(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetFlushMode() {
        query.getFlushMode();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLockMode() {
        query.getLockMode();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnwrap() {
        query.unwrap(Widget.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetMaxResults() {
        query.setMaxResults(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFirstResult() {
        query.setFirstResult(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetHint() {
        query.setHint("", null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetParameter1() {
        query.setParameter((Parameter<Object>) null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetParameter2() {
        query.setParameter((Parameter<Calendar>) null, (Calendar) null, TemporalType.DATE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetParameter3() {
        query.setParameter((Parameter<Date>) null, (Date) null, TemporalType.DATE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetParameter4() {
        query.setParameter("", null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetParameter5() {
        query.setParameter("", (Calendar) null, TemporalType.DATE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetParameter6() {
        query.setParameter("", (Date) null, TemporalType.DATE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetParameter7() {
        query.setParameter(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetParameter8() {
        query.setParameter(0, (Calendar) null, TemporalType.DATE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetParameter() {
        query.setParameter(0, (Date) null, TemporalType.DATE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFlushMode() {
        query.setFlushMode(FlushModeType.AUTO);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetLockMode() {
        query.setLockMode(LockModeType.NONE);
    }
}
