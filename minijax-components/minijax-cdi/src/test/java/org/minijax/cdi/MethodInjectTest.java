package org.minijax.cdi;

import static org.junit.Assert.*;

import org.junit.Test;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

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