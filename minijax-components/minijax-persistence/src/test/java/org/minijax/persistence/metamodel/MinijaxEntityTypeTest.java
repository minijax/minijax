package org.minijax.persistence.metamodel;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import jakarta.persistence.metamodel.Attribute;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxEntityManagerFactory;
import org.minijax.persistence.MinijaxPersistenceProvider;
import org.minijax.persistence.testmodel.Widget;

class MinijaxEntityTypeTest {
    private MinijaxEntityManagerFactory emf;
    private MinijaxEntityManager em;
    private MinijaxEntityType<Widget> entityType;

    @BeforeEach
    public void setUp() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        emf = provider.createEntityManagerFactory("testdb", null);
        em = emf.createEntityManager();
        entityType = em.getMetamodel().entity(Widget.class);
    }

    @AfterEach
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    void testBasic() {
        final Set<Attribute<? super Widget, ?>> attributes = entityType.getAttributes();
        assertEquals(2, attributes.size());
    }

    /*
     * Unsupported
     */

    @Test
    void testgetVersion() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getVersion(null));
    }

    @Test
    void testgetDeclaredVersion() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredVersion(null));
    }

    @Test
    void testgetSupertype() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getSupertype());
    }

    @Test
    void testhasSingleIdAttribute() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.hasSingleIdAttribute());
    }

    @Test
    void testhasVersionAttribute() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.hasVersionAttribute());
    }

    @Test
    void testgetIdClassAttributes() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getIdClassAttributes());
    }

    @Test
    void testgetPersistenceType() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getPersistenceType());
    }

    @Test
    void testgetBindableType() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getBindableType());
    }

    @Test
    void testgetBindableJavaType() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getBindableJavaType());
    }

    @Test
    void testgetDeclaredAttributes() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredAttributes());
    }

    @Test
    void testgetSingularAttribute1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getSingularAttribute(null, null));
    }

    @Test
    void testgetDeclaredSingularAttribute1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredSingularAttribute(null, null));
    }

    @Test
    void testgetSingularAttributes() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getSingularAttributes());
    }

    @Test
    void testgetDeclaredSingularAttributes() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredSingularAttributes());
    }

    @Test
    void testgetCollection1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getCollection(null, null));
    }

    @Test
    void testgetDeclaredCollection1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredCollection(null, null));
    }

    @Test
    void testgetSet1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getSet(null, null));
    }

    @Test
    void testgetDeclaredSet1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredSet(null, null));
    }

    @Test
    void testgetList1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getList(null, null));
    }

    @Test
    void testgetDeclaredList1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredList(null, null));
    }

    @Test
    void testgetMap1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getMap(null, null, null));
    }

    @Test
    void testgetDeclaredMap1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredMap(null, null, null));
    }

    @Test
    void testgetPluralAttributes() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getPluralAttributes());
    }

    @Test
    void testgetDeclaredPluralAttributes() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredPluralAttributes());
    }

    @Test
    void testgetDeclaredAttribute() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredAttribute(null));
    }

    @Test
    void testgetSingularAttribute() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getSingularAttribute(null));
    }

    @Test
    void testgetDeclaredSingularAttribute() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredSingularAttribute(null));
    }

    @Test
    void testgetCollection() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getCollection(null));
    }

    @Test
    void testgetDeclaredCollection() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredCollection(null));
    }

    @Test
    void testgetSet() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getSet(null));
    }

    @Test
    void testgetDeclaredSet() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredSet(null));
    }

    @Test
    void testgetList() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getList(null));
    }

    @Test
    void testgetDeclaredList() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredList(null));
    }

    @Test
    void testgetMap() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getMap(null));
    }

    @Test
    void testgetDeclaredMap() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredMap(null));
    }
}
