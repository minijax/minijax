package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;

class FeatureTest {

    public static class MyFeature implements Feature {
        static MyFeature lastInstance;

        @Override
        public boolean configure(final FeatureContext context) {
            lastInstance = this;
            return true;
        }
    }

    @Test
    void testFeature() {
        final Minijax minijax = new Minijax();
        minijax.register(MyFeature.class);
        assertNotNull(MyFeature.lastInstance);
    }
}
