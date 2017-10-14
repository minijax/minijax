package org.minijax;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class InheritanceTest extends MinijaxTest {

    public static abstract class MyBaseClass {
        public boolean baseInjected;

        @Inject
        public void baseInject() {
            baseInjected = true;
        }
    }

    @Singleton
    public static class MySubClass extends MyBaseClass {
        public boolean subInjected;

        @Inject
        public void subInject() {
            subInjected = true;
        }
    }


    @Test
    public void testInheritanceInject() {
        register(MySubClass.class);

        final MySubClass r = getServer().get(MySubClass.class, null, null);
        assertNotNull(r);
        assertTrue(r.subInjected);
        assertTrue(r.baseInjected);
    }
}
