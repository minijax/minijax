package org.minijax.persistence.criteria;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.CollectionAttribute;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxEntityManagerFactory;
import org.minijax.persistence.MinijaxPersistenceProvider;
import org.minijax.persistence.testmodel.Widget;

public class MinijaxRootTest {
    private MinijaxEntityManagerFactory emf;
    private MinijaxEntityManager em;
    private MinijaxRoot<Widget> root;

    @BeforeEach
    public void setUp() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        emf = provider.createEntityManagerFactory("testdb", null);
        em = emf.createEntityManager();
        root = new MinijaxRoot<>(em.getMetamodel().entity(Widget.class));
    }

    @AfterEach
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void testgetJoins() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.getJoins();
    });
    }

    @Test
    public void testisCorrelated() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.isCorrelated();
    });
    }

    @Test
    public void testgetCorrelationParent() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.getCorrelationParent();
    });
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin1() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.join((SingularAttribute) null);
    });
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin2() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.join((SingularAttribute) null, (JoinType) null);
    });
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin3() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.join((CollectionAttribute) null);
    });
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin4() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.join((SetAttribute) null);
    });
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin5() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.join((ListAttribute) null);
    });
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin6() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.join((MapAttribute) null);
    });
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin7() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.join((CollectionAttribute) null, (JoinType) null);
    });
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin8() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.join((SetAttribute) null, (JoinType) null);
    });
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin9() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.join((ListAttribute) null, (JoinType) null);
    });
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin10() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.join((MapAttribute) null, (JoinType) null);
    });
    }

    @Test
    public void testjoin11() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.join((String) null);
    });
    }

    @Test
    public void testjoinCollection1() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.joinCollection(null);
    });
    }

    @Test
    public void testjoinSet1() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.joinSet(null);
    });
    }

    @Test
    public void testjoinList1() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.joinList(null);
    });
    }

    @Test
    public void testjoinMap1() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.joinMap(null);
    });
    }

    @Test
    public void testjoin12() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.join((String) null, (JoinType) null);
    });
    }

    @Test
    public void testjoinCollection2() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.joinCollection((String) null, (JoinType) null);
    });
    }

    @Test
    public void testjoinSet2() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.joinSet((String) null, (JoinType) null);
    });
    }

    @Test
    public void testjoinList2() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.joinList((String) null, (JoinType) null);
    });
    }

    @Test
    public void testjoinMap2() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.joinMap((String) null, (JoinType) null);
    });
    }

    @Test
    public void testgetFetches() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.getFetches();
    });
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testfetch1() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.fetch((SingularAttribute) null);
    });
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testfetch2() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.fetch((SingularAttribute) null, (JoinType) null);
    });
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testfetch3() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.fetch((PluralAttribute) null);
    });
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testfetch4() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.fetch((PluralAttribute) null, (JoinType) null);
    });
    }

    @Test
    public void testfetch5() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.fetch((String) null);
    });
    }

    @Test
    public void testfetch6() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.fetch((String) null, (JoinType) null);
    });
    }

    @Test
    public void testgetModel() {
        assertThrows(UnsupportedOperationException.class, () -> {
        root.getModel();
    });
    }
}
