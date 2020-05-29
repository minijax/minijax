package org.minijax.persistence.criteria;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.CollectionAttribute;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxEntityManagerFactory;
import org.minijax.persistence.MinijaxPersistenceProvider;
import org.minijax.persistence.testmodel.Widget;

public class MinijaxRootTest {
    private MinijaxEntityManagerFactory emf;
    private MinijaxEntityManager em;
    private MinijaxRoot<Widget> root;

    @Before
    public void setUp() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        emf = provider.createEntityManagerFactory("testdb", null);
        em = emf.createEntityManager();
        root = new MinijaxRoot<>(em.getMetamodel().entity(Widget.class));
    }

    @After
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetJoins() {
        root.getJoins();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testisCorrelated() {
        root.isCorrelated();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetCorrelationParent() {
        root.getCorrelationParent();
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin1() {
        root.join((SingularAttribute) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin2() {
        root.join((SingularAttribute) null, (JoinType) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin3() {
        root.join((CollectionAttribute) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin4() {
        root.join((SetAttribute) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin5() {
        root.join((ListAttribute) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin6() {
        root.join((MapAttribute) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin7() {
        root.join((CollectionAttribute) null, (JoinType) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin8() {
        root.join((SetAttribute) null, (JoinType) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin9() {
        root.join((ListAttribute) null, (JoinType) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testjoin10() {
        root.join((MapAttribute) null, (JoinType) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testjoin11() {
        root.join((String) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testjoinCollection1() {
        root.joinCollection(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testjoinSet1() {
        root.joinSet(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testjoinList1() {
        root.joinList(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testjoinMap1() {
        root.joinMap(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testjoin12() {
        root.join((String) null, (JoinType) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testjoinCollection2() {
        root.joinCollection((String) null, (JoinType) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testjoinSet2() {
        root.joinSet((String) null, (JoinType) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testjoinList2() {
        root.joinList((String) null, (JoinType) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testjoinMap2() {
        root.joinMap((String) null, (JoinType) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetFetches() {
        root.getFetches();
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testfetch1() {
        root.fetch((SingularAttribute) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testfetch2() {
        root.fetch((SingularAttribute) null, (JoinType) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testfetch3() {
        root.fetch((PluralAttribute) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testfetch4() {
        root.fetch((PluralAttribute) null, (JoinType) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testfetch5() {
        root.fetch((String) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testfetch6() {
        root.fetch((String) null, (JoinType) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetModel() {
        root.getModel();
    }
}
