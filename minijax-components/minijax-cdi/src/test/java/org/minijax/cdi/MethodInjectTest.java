package org.minijax.cdi;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.junit.Test;

public class MethodInjectTest {

    @Singleton
    static class MethodInjectResource {
        boolean injected;

        @Inject
        public void injectMe() {
            injected = true;
        }
    }

    @Test
    public void testMethodInject() {
        try (final MinijaxInjector injector = new MinijaxInjector()) {
            final MethodInjectResource r = injector.getResource(MethodInjectResource.class);
            assertNotNull(r);
            assertTrue(r.injected);
        }
    }
}