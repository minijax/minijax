package org.minijax.rs.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.ws.rs.core.FeatureContext;

import org.junit.jupiter.api.Test;

class PersistenceFeatureTest {

    @Test
    void testNotMinijax() {
        assertThrows(IllegalArgumentException.class, () -> {
            final PersistenceFeature feature = new PersistenceFeature();
            feature.configure(mock(FeatureContext.class));
        });
    }
}
