package org.minijax;

import java.util.Map;

import org.junit.Test;

public class DefaultConfigurableTest {

    public static class MyConfigurable extends MinijaxDefaultConfigurable<MyConfigurable> {
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetConfiguration() {
        final MyConfigurable c = new MyConfigurable();
        c.getConfiguration();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testProperty() {
        final MyConfigurable c = new MyConfigurable();
        c.property(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister1() {
        final MyConfigurable c = new MyConfigurable();
        c.register((Class<?>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister2() {
        final MyConfigurable c = new MyConfigurable();
        c.register((Class<?>) null, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister3() {
        final MyConfigurable c = new MyConfigurable();
        c.register((Class<?>) null, (Class<?>[]) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister4() {
        final MyConfigurable c = new MyConfigurable();
        c.register((Class<?>) null, (Map<Class<?>, Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister5() {
        final MyConfigurable c = new MyConfigurable();
        c.register((Object) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister6() {
        final MyConfigurable c = new MyConfigurable();
        c.register((Object) null, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister7() {
        final MyConfigurable c = new MyConfigurable();
        c.register((Object) null, (Class<?>[]) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister8() {
        final MyConfigurable c = new MyConfigurable();
        c.register((Object) null, (Map<Class<?>, Integer>) null);
    }
}
