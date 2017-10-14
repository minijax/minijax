package org.minijax;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class MethodInjectTest extends MinijaxTest {

    @Singleton
    public static class MethodInjectResource {
        public boolean injected;

        @Inject
        public void injectMe() {
            injected = true;
        }
    }

    @Test
    public void testMethodInject() {
        register(MethodInjectResource.class);

        final MethodInjectResource r = getServer().get(MethodInjectResource.class, null, null);
        assertNotNull(r);
        assertTrue(r.injected);
    }
}
