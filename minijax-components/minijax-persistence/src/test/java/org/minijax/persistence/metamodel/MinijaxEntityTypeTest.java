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

public class MinijaxEntityTypeTest {
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
    public void testBasic() {
        final Set<Attribute<? super Widget, ?>> attributes = entityType.getAttributes();
        assertEquals(2, attributes.size());
    }

    /*
     * Unsupported
     */

    @Test
    public void testgetVersion() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getVersion(null));
    }

    @Test
    public void testgetDeclaredVersion() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredVersion(null));
    }

    @Test
    public void testgetSupertype() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getSupertype());
    }

    @Test
    public void testhasSingleIdAttribute() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.hasSingleIdAttribute());
    }

    @Test
    public void testhasVersionAttribute() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.hasVersionAttribute());
    }

    @Test
    public void testgetIdClassAttributes() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getIdClassAttributes());
    }

    @Test
    public void testgetPersistenceType() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getPersistenceType());
    }

    @Test
    public void testgetBindableType() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getBindableType());
    }

    @Test
    public void testgetBindableJavaType() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getBindableJavaType());
    }

    @Test
    public void testgetDeclaredAttributes() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredAttributes());
    }

    @Test
    public void testgetSingularAttribute1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getSingularAttribute(null, null));
    }

    @Test
    public void testgetDeclaredSingularAttribute1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredSingularAttribute(null, null));
    }

    @Test
    public void testgetSingularAttributes() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getSingularAttributes());
    }

    @Test
    public void testgetDeclaredSingularAttributes() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredSingularAttributes());
    }

    @Test
    public void testgetCollection1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getCollection(null, null));
    }

    @Test
    public void testgetDeclaredCollection1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredCollection(null, null));
    }

    @Test
    public void testgetSet1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getSet(null, null));
    }

    @Test
    public void testgetDeclaredSet1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredSet(null, null));
    }

    @Test
    public void testgetList1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getList(null, null));
    }

    @Test
    public void testgetDeclaredList1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredList(null, null));
    }

    @Test
    public void testgetMap1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getMap(null, null, null));
    }

    @Test
    public void testgetDeclaredMap1() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredMap(null, null, null));
    }

    @Test
    public void testgetPluralAttributes() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getPluralAttributes());
    }

    @Test
    public void testgetDeclaredPluralAttributes() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredPluralAttributes());
    }

    @Test
    public void testgetDeclaredAttribute() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredAttribute(null));
    }

    @Test
    public void testgetSingularAttribute() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getSingularAttribute(null));
    }

    @Test
    public void testgetDeclaredSingularAttribute() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredSingularAttribute(null));
    }

    @Test
    public void testgetCollection() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getCollection(null));
    }

    @Test
    public void testgetDeclaredCollection() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredCollection(null));
    }

    @Test
    public void testgetSet() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getSet(null));
    }

    @Test
    public void testgetDeclaredSet() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredSet(null));
    }

    @Test
    public void testgetList() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getList(null));
    }

    @Test
    public void testgetDeclaredList() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredList(null));
    }

    @Test
    public void testgetMap() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getMap(null));
    }

    @Test
    public void testgetDeclaredMap() {
        assertThrows(UnsupportedOperationException.class, () -> entityType.getDeclaredMap(null));
    }
}
