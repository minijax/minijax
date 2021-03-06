package org.minijax.cdi;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.enterprise.inject.InjectionException;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InjectorTest {
    private MinijaxInjector injector;

    @BeforeEach
    public void setUp() {
        injector = new MinijaxInjector();
    }

    @AfterEach
    public void tearDown() {
        injector.close();
    }

    static class A {
        @Inject
        B b;
    }

    static class B {
        final C c;

        @Inject
        B(final C c) {
            this.c = c;
        }
    }

    static class C {
        int x;
    }

    @Test
    void testSimple() {
        final Provider<C> provider = injector.getProvider(C.class);
        assertNotNull(provider);

        final C instance = provider.get();
        assertNotNull(instance);

        final Provider<C> provider2 = injector.getProvider(C.class);
        assertNotNull(provider2);
        assertEquals(provider, provider2);

        final C instance2 = provider2.get();
        assertNotNull(instance2);
        assertNotEquals(instance, instance2);
    }

    @Test
    void testParamInject() {
        final Provider<B> provider = injector.getProvider(B.class);
        assertNotNull(provider);

        final B instance = provider.get();
        assertNotNull(instance);
        assertNotNull(instance.c);
    }

    @Test
    void testFieldInject() {
        final Provider<A> provider = injector.getProvider(A.class);
        assertNotNull(provider);

        final A instance = provider.get();
        assertNotNull(instance);
        assertNotNull(instance.b);
        assertNotNull(instance.b.c);
    }

    static class ProviderParamInjection {
        final C c;

        @Inject
        ProviderParamInjection(final Provider<C> provider) {
            c = provider.get();
        }
    }

    @Test
    void testProviderParamInjection() {
        final Provider<ProviderParamInjection> providerInjectionProvider = injector
                .getProvider(ProviderParamInjection.class);
        assertNotNull(providerInjectionProvider);

        final ProviderParamInjection providerInjection = providerInjectionProvider.get();
        assertNotNull(providerInjection);
        assertNotNull(providerInjection.c);
    }

    static class ProviderFieldInjection {
        @Inject
        Provider<C> provider;
    }

    @Test
    void testProviderFieldInjection() {
        final Provider<ProviderFieldInjection> providerInjectionProvider = injector
                .getProvider(ProviderFieldInjection.class);
        assertNotNull(providerInjectionProvider);

        final ProviderFieldInjection providerInjection = providerInjectionProvider.get();
        assertNotNull(providerInjection);
        assertNotNull(providerInjection.provider);

        final C instance1 = providerInjection.provider.get();
        assertNotNull(instance1);

        final C instance2 = providerInjection.provider.get();
        assertNotNull(instance2);
        assertNotEquals(instance1, instance2);
    }

    @Singleton
    private static class MySingleton {
    }

    @Test
    void testSingleton() {
        final Provider<MySingleton> provider = injector.getProvider(MySingleton.class);
        assertNotNull(provider);

        final MySingleton instance = provider.get();
        assertNotNull(instance);

        final Provider<MySingleton> provider2 = injector.getProvider(MySingleton.class);
        assertNotNull(provider2);
        assertEquals(provider, provider2);

        final MySingleton instance2 = provider2.get();
        assertNotNull(instance2);
        assertEquals(instance, instance2);
        assertSame(instance, instance2);
    }

    @Test
    void testRegisterSingleton() {
        final MySingleton inst = new MySingleton();
        injector.bind(inst, MySingleton.class);
        assertTrue(injector.getSingletons().contains(inst));
    }

    static class NoValidConstructors {
        NoValidConstructors(final int x) {
        }
    }

    @Test
    void testNoValidConstructors() {
        assertThrows(InjectionException.class, () -> injector.getProvider(NoValidConstructors.class));
    }

    static class MultipleInjectConstructors {
        @Inject
        MultipleInjectConstructors(final int x) {
        }

        @Inject
        MultipleInjectConstructors(final String x) {
        }
    }

    @Test
    void testMultipleInjectConstructors() {
        assertThrows(InjectionException.class, () -> injector.getProvider(MultipleInjectConstructors.class));
    }

    static class ParamCircularDependencyA {
        @Inject
        ParamCircularDependencyA(final ParamCircularDependencyB b) {
        }
    }

    private static class ParamCircularDependencyB {
        @Inject
        ParamCircularDependencyB(final ParamCircularDependencyA a) {
        }
    }

    @Test
    void testParamCircularDependency() {
        assertThrows(InjectionException.class, () -> injector.getProvider(ParamCircularDependencyA.class));
    }

    static class FieldCircularDependencyA {
        @Inject
        FieldCircularDependencyB b;
    }

    static class FieldCircularDependencyB {
        @Inject
        FieldCircularDependencyA a;
    }

    @Test
    void testFieldCircularDependency() {
        assertThrows(InjectionException.class, () -> injector.getProvider(FieldCircularDependencyA.class));
    }

    static class ExplodingConstructor {
        ExplodingConstructor() {
            throw new IllegalStateException("boom");
        }
    }

    @Test
    void testExplodingConstructor() {
        assertThrows(InjectionException.class, () -> injector.getResource(ExplodingConstructor.class));
    }

    static class ExplodingSetter {
        @Inject
        public void setFoo(final C foo) {
            throw new IllegalArgumentException("boom");
        }
    }

    @Test
    void testNewExplodingSetter() {
        assertThrows(InjectionException.class, () -> injector.getResource(ExplodingSetter.class));
    }

    @Test
    void testInitExplodingSetter() {
        final ExplodingSetter instance = new ExplodingSetter();
        assertThrows(InjectionException.class, () -> injector.initResource(instance, null));
    }
}
