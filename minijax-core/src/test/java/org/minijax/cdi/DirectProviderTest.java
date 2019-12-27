package org.minijax.cdi;

import static org.junit.Assert.*;

import javax.inject.Provider;

import org.junit.Test;
import org.minijax.Minijax;

public class DirectProviderTest {

    public static class MyWidget {
        public int a;
    }

    @javax.ws.rs.ext.Provider
    public static class MyWidgetProvider implements Provider<MyWidget> {
        @Override
        public MyWidget get() {
            final MyWidget widget = new MyWidget();
            widget.a = 42;
            return widget;
        }
    }

    @Test
    public void testDirectProviderClass() {
        try (final MinijaxInjector injector = new MinijaxInjector()) {
            injector.register(MyWidgetProvider.class, MyWidget.class);

            final Provider<MyWidget> provider = injector.getProvider(MyWidget.class);
            assertNotNull(provider);

            final MyWidget resource = injector.getResource(MyWidget.class);
            assertNotNull(resource);
            assertEquals(42, resource.a);
        }
    }

    @Test
    public void testDirectProviderSingleton() {
        try (final MinijaxInjector injector = new MinijaxInjector()) {
            injector.register(new MyWidgetProvider(), MyWidget.class);

            final Provider<MyWidget> provider = injector.getProvider(MyWidget.class);
            assertNotNull(provider);

            final MyWidget resource = injector.getResource(MyWidget.class);
            assertNotNull(resource);
            assertEquals(42, resource.a);
        }
    }

    @Test
    public void testAutoScanProvider() {
        final Minijax minijax = new Minijax();
        minijax.packages("org.minijax.cdi");

        final MinijaxInjector injector = minijax.getDefaultApplication().getInjector();

        final Provider<MyWidget> provider = injector.getProvider(MyWidget.class);
        assertNotNull(provider);

        final MyWidget resource = injector.getResource(MyWidget.class);
        assertNotNull(resource);
        assertEquals(42, resource.a);
    }
}
