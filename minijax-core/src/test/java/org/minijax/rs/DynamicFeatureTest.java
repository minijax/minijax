package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;

public class DynamicFeatureTest {

    public static class MyFeature implements DynamicFeature {
        static MyFeature lastInstance;

        @Override
        public void configure(final ResourceInfo resourceinfo, final FeatureContext context) {
            lastInstance = this;
        }
    }

    @Path("/dynamicfeaturetest")
    public static class MyResource {
        @GET
        public static String hello() {
            return "Hello";
        }
    }

    @Test
    public void testFeature() {
        final Minijax minijax = new Minijax();

        minijax.register(MyFeature.class);
        assertNull(MyFeature.lastInstance);

        minijax.register(MyResource.class);
        assertNotNull(MyFeature.lastInstance);
    }
}
