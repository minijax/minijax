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

class MinijaxRootTest {
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
    void testgetJoins() {
        assertThrows(UnsupportedOperationException.class, () -> root.getJoins());
    }

    @Test
    void testisCorrelated() {
        assertThrows(UnsupportedOperationException.class, () -> root.isCorrelated());
    }

    @Test
    void testgetCorrelationParent() {
        assertThrows(UnsupportedOperationException.class, () -> root.getCorrelationParent());
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testjoin1() {
        assertThrows(UnsupportedOperationException.class, () -> root.join((SingularAttribute) null));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testjoin2() {
        assertThrows(UnsupportedOperationException.class, () -> root.join((SingularAttribute) null, null));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testjoin3() {
        assertThrows(UnsupportedOperationException.class, () -> root.join((CollectionAttribute) null));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testjoin4() {
        assertThrows(UnsupportedOperationException.class, () -> root.join((SetAttribute) null));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testjoin5() {
        assertThrows(UnsupportedOperationException.class, () -> root.join((ListAttribute) null));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testjoin6() {
        assertThrows(UnsupportedOperationException.class, () -> root.join((MapAttribute) null));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testjoin7() {
        assertThrows(UnsupportedOperationException.class, () -> root.join((CollectionAttribute) null, null));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testjoin8() {
        assertThrows(UnsupportedOperationException.class, () -> root.join((SetAttribute) null, null));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testjoin9() {
        assertThrows(UnsupportedOperationException.class, () -> root.join((ListAttribute) null, null));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testjoin10() {
        assertThrows(UnsupportedOperationException.class, () -> root.join((MapAttribute) null, null));
    }

    @Test
    void testjoin11() {
        assertThrows(UnsupportedOperationException.class, () -> root.join((String) null));
    }

    @Test
    void testjoinCollection1() {
        assertThrows(UnsupportedOperationException.class, () -> root.joinCollection(null));
    }

    @Test
    void testjoinSet1() {
        assertThrows(UnsupportedOperationException.class, () -> root.joinSet(null));
    }

    @Test
    void testjoinList1() {
        assertThrows(UnsupportedOperationException.class, () -> root.joinList(null));
    }

    @Test
    void testjoinMap1() {
        assertThrows(UnsupportedOperationException.class, () -> root.joinMap(null));
    }

    @Test
    void testjoin12() {
        assertThrows(UnsupportedOperationException.class, () -> root.join((String) null, null));
    }

    @Test
    void testjoinCollection2() {
        assertThrows(UnsupportedOperationException.class, () -> root.joinCollection(null, null));
    }

    @Test
    void testjoinSet2() {
        assertThrows(UnsupportedOperationException.class, () -> root.joinSet(null, null));
    }

    @Test
    void testjoinList2() {
        assertThrows(UnsupportedOperationException.class, () -> root.joinList(null, null));
    }

    @Test
    void testjoinMap2() {
        assertThrows(UnsupportedOperationException.class, () -> root.joinMap(null, null));
    }

    @Test
    void testgetFetches() {
        assertThrows(UnsupportedOperationException.class, () -> root.getFetches());
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testfetch1() {
        assertThrows(UnsupportedOperationException.class, () -> root.fetch((SingularAttribute) null));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testfetch2() {
        assertThrows(UnsupportedOperationException.class, () -> root.fetch((SingularAttribute) null, null));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testfetch3() {
        assertThrows(UnsupportedOperationException.class, () -> root.fetch((PluralAttribute) null));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testfetch4() {
        assertThrows(UnsupportedOperationException.class, () -> root.fetch((PluralAttribute) null, null));
    }

    @Test
    void testfetch5() {
        assertThrows(UnsupportedOperationException.class, () -> root.fetch((String) null));
    }

    @Test
    void testfetch6() {
        assertThrows(UnsupportedOperationException.class, () -> root.fetch((String) null, null));
    }

    @Test
    void testgetModel() {
        assertThrows(UnsupportedOperationException.class, () -> root.getModel());
    }
}
