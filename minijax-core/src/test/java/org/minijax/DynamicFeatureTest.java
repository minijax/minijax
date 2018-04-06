package org.minijax;

import static org.junit.Assert.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

import org.junit.Test;

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
