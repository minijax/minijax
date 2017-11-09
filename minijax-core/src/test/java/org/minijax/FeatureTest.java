package org.minijax;

import static org.junit.Assert.*;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.junit.Test;

public class FeatureTest {

    public static class MyFeature implements Feature {
        static MyFeature lastInstance;

        @Override
        public boolean configure(final FeatureContext context) {
            lastInstance = this;
            return true;
        }
    }

    @Test
    public void testFeature() {
        final Minijax minijax = new Minijax();
        minijax.register(MyFeature.class);
        assertNotNull(MyFeature.lastInstance);
    }
}
