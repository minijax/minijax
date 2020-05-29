package org.minijax.persistence.metamodel;

import static org.junit.Assert.*;

import java.util.Set;

import jakarta.persistence.metamodel.Attribute;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxEntityManagerFactory;
import org.minijax.persistence.MinijaxPersistenceProvider;
import org.minijax.persistence.testmodel.Widget;

public class MinijaxEntityTypeTest {
    private MinijaxEntityManagerFactory emf;
    private MinijaxEntityManager em;
    private MinijaxEntityType<Widget> entityType;

    @Before
    public void setUp() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        emf = provider.createEntityManagerFactory("testdb", null);
        em = emf.createEntityManager();
        entityType = em.getMetamodel().entity(Widget.class);
    }

    @After
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void testBasic() {
        final Set<Attribute<? super Widget, ?>> attributes = entityType.getAttributes();
        assertEquals(2, attributes.size());
    }

    /*
     * Unsupported
     */

    @Test(expected = UnsupportedOperationException.class)
    public void testgetVersion() {
        entityType.getVersion(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDeclaredVersion() {
        entityType.getDeclaredVersion(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetSupertype() {
        entityType.getSupertype();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testhasSingleIdAttribute() {
        entityType.hasSingleIdAttribute();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testhasVersionAttribute() {
        entityType.hasVersionAttribute();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetIdClassAttributes() {
        entityType.getIdClassAttributes();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetPersistenceType() {
        entityType.getPersistenceType();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetBindableType() {
        entityType.getBindableType();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetBindableJavaType() {
        entityType.getBindableJavaType();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDeclaredAttributes() {
        entityType.getDeclaredAttributes();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetSingularAttribute1() {
        entityType.getSingularAttribute(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDeclaredSingularAttribute1() {
        entityType.getDeclaredSingularAttribute(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetSingularAttributes() {
        entityType.getSingularAttributes();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDeclaredSingularAttributes() {
        entityType.getDeclaredSingularAttributes();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetCollection1() {
        entityType.getCollection(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDeclaredCollection1() {
        entityType.getDeclaredCollection(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetSet1() {
        entityType.getSet(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDeclaredSet1() {
        entityType.getDeclaredSet(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetList1() {
        entityType.getList(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDeclaredList1() {
        entityType.getDeclaredList(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMap1() {
        entityType.getMap(null, null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDeclaredMap1() {
        entityType.getDeclaredMap(null, null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetPluralAttributes() {
        entityType.getPluralAttributes();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDeclaredPluralAttributes() {
        entityType.getDeclaredPluralAttributes();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDeclaredAttribute() {
        entityType.getDeclaredAttribute(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetSingularAttribute() {
        entityType.getSingularAttribute(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDeclaredSingularAttribute() {
        entityType.getDeclaredSingularAttribute(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetCollection() {
        entityType.getCollection(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDeclaredCollection() {
        entityType.getDeclaredCollection(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetSet() {
        entityType.getSet(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDeclaredSet() {
        entityType.getDeclaredSet(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetList() {
        entityType.getList(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDeclaredList() {
        entityType.getDeclaredList(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMap() {
        entityType.getMap(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDeclaredMap() {
        entityType.getDeclaredMap(null);
    }
}
