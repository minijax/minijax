package org.minijax.persistence.metamodel;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxEntityManagerFactory;
import org.minijax.persistence.MinijaxPersistenceProvider;
import org.minijax.persistence.testmodel.Widget;

public class MinijaxEntityTypeTest {

    @Test
    public void testBasic() {
        final MinijaxPersistenceProvider spi = new MinijaxPersistenceProvider();
        final MinijaxEntityManagerFactory emf = spi.createEntityManagerFactory("testdb", null);
        try (final MinijaxEntityManager em = emf.createEntityManager()) {
            final MinijaxEntityType<Widget> entityType = em.getMetamodel().entity(Widget.class);
            assertNotNull(entityType);

            final Set<?> attributes = entityType.getAttributes();
            assertEquals(2, attributes.size());
        }
    }
}
