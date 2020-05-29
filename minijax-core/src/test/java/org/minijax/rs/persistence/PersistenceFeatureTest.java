package org.minijax.rs.persistence;

import static org.mockito.Mockito.*;

import jakarta.ws.rs.core.FeatureContext;

import org.junit.Test;
import org.minijax.rs.persistence.PersistenceFeature;

public class PersistenceFeatureTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNotMinijax() {
        final PersistenceFeature feature = new PersistenceFeature();
        feature.configure(mock(FeatureContext.class));
    }
}
