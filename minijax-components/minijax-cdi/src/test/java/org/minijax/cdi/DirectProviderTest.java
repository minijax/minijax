package org.minijax.cdi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import jakarta.inject.Provider;

class DirectProviderTest {

    public static class MyWidget {
        public int a;
    }

    public static class MyWidgetProvider implements Provider<MyWidget> {
        @Override
        public MyWidget get() {
            final MyWidget widget = new MyWidget();
            widget.a = 42;
            return widget;
        }
    }

    @Test
    void testDirectProviderClass() {
        try (final MinijaxInjector injector = new MinijaxInjector()) {
            injector.bind(MyWidgetProvider.class, MyWidget.class);

            final Provider<MyWidget> provider = injector.getProvider(MyWidget.class);
            assertNotNull(provider);

            final MyWidget resource = injector.getResource(MyWidget.class);
            assertNotNull(resource);
            assertEquals(42, resource.a);
        }
    }

    @Test
    void testDirectProviderSingleton() {
        try (final MinijaxInjector injector = new MinijaxInjector()) {
            injector.bind(new MyWidgetProvider(), MyWidget.class);

            final Provider<MyWidget> provider = injector.getProvider(MyWidget.class);
            assertNotNull(provider);

            final MyWidget resource = injector.getResource(MyWidget.class);
            assertNotNull(resource);
            assertEquals(42, resource.a);
        }
    }
}
