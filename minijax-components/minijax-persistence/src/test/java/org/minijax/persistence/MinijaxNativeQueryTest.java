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

public class MinijaxNativeQueryTest {
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
    public void testBasic() {
    }

    /*
     * Unsupported
     */

    @Test
    public void testExecuteUpdate() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.executeUpdate();
    });
    }

    @Test
    public void testGetMaxResults() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.getMaxResults();
    });
    }

    @Test
    public void testGetFirstResult() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.getFirstResult();
    });
    }

    @Test
    public void testGetHints() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.getHints();
    });
    }

    @Test
    public void testGetParameters() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.getParameters();
    });
    }

    @Test
    public void testGetParameter1() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.getParameter("");
    });
    }

    @Test
    public void testGetParameter2() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.getParameter("", Widget.class);
    });
    }

    @Test
    public void testGetParameter3() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.getParameter(0);
    });
    }

    @Test
    public void testGetParameter4() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.getParameter(0, Widget.class);
    });
    }

    @Test
    public void testIsBound() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.isBound(null);
    });
    }

    @Test
    public void testGetParameterValue1() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.getParameterValue((Parameter<Object>) null);
    });
    }

    @Test
    public void testGetParameterValue2() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.getParameterValue("");
    });
    }

    @Test
    public void testGetParameterValue3() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.getParameterValue(0);
    });
    }

    @Test
    public void testGetFlushMode() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.getFlushMode();
    });
    }

    @Test
    public void testGetLockMode() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.getLockMode();
    });
    }

    @Test
    public void testUnwrap() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.unwrap(Widget.class);
    });
    }

    @Test
    public void testSetMaxResults() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.setMaxResults(0);
    });
    }

    @Test
    public void testSetFirstResult() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.setFirstResult(0);
    });
    }

    @Test
    public void testSetHint() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.setHint("", null);
    });
    }

    @Test
    public void testSetParameter1() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.setParameter((Parameter<Object>) null, null);
    });
    }

    @Test
    public void testSetParameter2() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.setParameter((Parameter<Calendar>) null, (Calendar) null, TemporalType.DATE);
    });
    }

    @Test
    public void testSetParameter3() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.setParameter((Parameter<Date>) null, (Date) null, TemporalType.DATE);
    });
    }

    @Test
    public void testSetParameter4() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.setParameter("", null);
    });
    }

    @Test
    public void testSetParameter5() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.setParameter("", (Calendar) null, TemporalType.DATE);
    });
    }

    @Test
    public void testSetParameter6() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.setParameter("", (Date) null, TemporalType.DATE);
    });
    }

    @Test
    public void testSetParameter7() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.setParameter(0, null);
    });
    }

    @Test
    public void testSetParameter8() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.setParameter(0, (Calendar) null, TemporalType.DATE);
    });
    }

    @Test
    public void testSetParameter() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.setParameter(0, (Date) null, TemporalType.DATE);
    });
    }

    @Test
    public void testSetFlushMode() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.setFlushMode(FlushModeType.AUTO);
    });
    }

    @Test
    public void testSetLockMode() {
        assertThrows(UnsupportedOperationException.class, () -> {
        query.setLockMode(LockModeType.NONE);
    });
    }
}
