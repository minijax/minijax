package org.minijax.cdi;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Context;

import org.junit.Test;

public class InjectorTest {

    public static class A {
        @Inject
        B b;
    }

    public static class B {
        C c;

        @Inject
        public B(final C c) {
            this.c = c;
        }
    }

    public static class C {
        int x;
    }

    @Test
    public void testSimple() {
        final MinijaxInjector injector = new MinijaxInjector();

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
    public void testParamInject() {
        final MinijaxInjector injector = new MinijaxInjector();

        final Provider<B> provider = injector.getProvider(B.class);
        assertNotNull(provider);

        final B instance = provider.get();
        assertNotNull(instance);
        assertNotNull(instance.c);
    }

    @Test
    public void testFieldInject() {
        final MinijaxInjector injector = new MinijaxInjector();

        final Provider<A> provider = injector.getProvider(A.class);
        assertNotNull(provider);

        final A instance = provider.get();
        assertNotNull(instance);
        assertNotNull(instance.b);
        assertNotNull(instance.b.c);
    }

    public static class ProviderParamInjection {
        C c;
        @Inject ProviderParamInjection(final Provider<C> provider) {
            c = provider.get();
        }
    }

    @Test
    public void testProviderParamInjection() {
        final MinijaxInjector injector = new MinijaxInjector();

        final Provider<ProviderParamInjection> providerInjectionProvider = injector.getProvider(ProviderParamInjection.class);
        assertNotNull(providerInjectionProvider);

        final ProviderParamInjection providerInjection = providerInjectionProvider.get();
        assertNotNull(providerInjection);
        assertNotNull(providerInjection.c);
    }

    public static class ProviderFieldInjection {
        @Inject Provider<C> provider;
    }

    @Test
    public void testProviderFieldInjection() {
        final MinijaxInjector injector = new MinijaxInjector();

        final Provider<ProviderFieldInjection> providerInjectionProvider = injector.getProvider(ProviderFieldInjection.class);
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
    public static class MySingleton {
        int x;
    }

    @Test
    public void testSingleton() {
        final MinijaxInjector injector = new MinijaxInjector();

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
        assertTrue(instance == instance2);
    }

    public static class NoValidConstructors {
        NoValidConstructors(final int x) { }
    }

    @Test(expected = InjectException.class)
    public void testNoValidConstructors() {
        final MinijaxInjector injector = new MinijaxInjector();
        injector.getProvider(NoValidConstructors.class);
    }

    public static class MultipleInjectConstructors {
        @Inject MultipleInjectConstructors(final int x) { }
        @Inject MultipleInjectConstructors(final String x) { }
    }

    @Test(expected = InjectException.class)
    public void testMultipleInjectConstructors() {
        final MinijaxInjector injector = new MinijaxInjector();
        injector.getProvider(MultipleInjectConstructors.class);
    }

    public static class ParamCircularDependencyA {
        @Inject ParamCircularDependencyA(final ParamCircularDependencyB b) { }
    }

    public static class ParamCircularDependencyB {
        @Inject ParamCircularDependencyB(final ParamCircularDependencyA a) { }
    }

    @Test(expected = InjectException.class)
    public void testParamCircularDependency() {
        final MinijaxInjector injector = new MinijaxInjector();
        injector.getProvider(ParamCircularDependencyA.class);
    }

    public static class FieldCircularDependencyA {
        @Inject FieldCircularDependencyB b;
    }

    public static class FieldCircularDependencyB {
        @Inject FieldCircularDependencyA a;
    }

    @Test(expected = InjectException.class)
    public void testFieldCircularDependency() {
        final MinijaxInjector injector = new MinijaxInjector();
        injector.getProvider(FieldCircularDependencyA.class);
    }

    public static class ExplodingConstructor {
        public ExplodingConstructor() {
            throw new IllegalStateException("boom");
        }
    }

    @Test(expected = InjectException.class)
    public void testExplodingConstructor() {
        final MinijaxInjector injector = new MinijaxInjector();
        injector.get(ExplodingConstructor.class);
    }

    public static class MultipleStrategies {
        @Context
        @HeaderParam("a")
        String a;
    }

    @Test(expected = InjectException.class)
    public void testMultipleStrategies() {
        final MinijaxInjector injector = new MinijaxInjector();
        injector.get(MultipleStrategies.class);
    }

    public static class MultipleNames {
        @Named("a")
        @HeaderParam("a")
        String a;
    }

    @Test(expected = InjectException.class)
    public void testMultipleNames() {
        final MinijaxInjector injector = new MinijaxInjector();
        injector.get(MultipleNames.class);
    }
}
