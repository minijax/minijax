package org.minijax.cdi;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class MethodInjectTest extends MinijaxTest {

    @Singleton
    static class MethodInjectResource {
        boolean injected;

        @Inject
        public void injectMe() {
            injected = true;
        }
    }

    @BeforeClass
    public static void setUpMethodInjectTest() {
        resetServer();
        register(MethodInjectResource.class);
    }

    @Test
    public void testMethodInject() {
        final MethodInjectResource r = getServer().getResource(MethodInjectResource.class);
        assertNotNull(r);
        assertTrue(r.injected);
    }
}