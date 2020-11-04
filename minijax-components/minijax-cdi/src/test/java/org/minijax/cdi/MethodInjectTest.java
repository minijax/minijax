package org.minijax.cdi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

class MethodInjectTest {

    @Singleton
    static class MethodInjectResource {
        boolean injected;

        @Inject
        public void injectMe() {
            injected = true;
        }
    }

    @Test
    void testMethodInject() {
        try (final MinijaxInjector injector = new MinijaxInjector()) {
            final MethodInjectResource r = injector.getResource(MethodInjectResource.class);
            assertNotNull(r);
            assertTrue(r.injected);
        }
    }
}