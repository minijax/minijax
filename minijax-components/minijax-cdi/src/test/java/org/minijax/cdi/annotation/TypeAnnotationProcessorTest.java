package org.minijax.cdi.annotation;

import static java.lang.annotation.RetentionPolicy.*;
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import org.junit.Test;
import org.minijax.cdi.MinijaxInjector;
import org.minijax.cdi.MinijaxProvider;

public class TypeAnnotationProcessorTest {

    @Scope
    @Retention(RUNTIME)
    public static @interface MyTypeAnnotation {
    }

    @MyTypeAnnotation
    public static class MyTestResource {
        Object foo;
    }

    public static class MyResourceProvider implements MinijaxProvider<MyTestResource> {

        @Override
        public MyTestResource get(final Object context) {
            final MyTestResource resource = new MyTestResource();
            resource.foo = "baz";
            return resource;
        }
    }

    public static class MyTypeAnnotationProcessor implements TypeAnnotationProcessor<MyTestResource> {

        @Override
        public MinijaxProvider<MyTestResource> buildProvider(final MinijaxProvider<MyTestResource> sourceProvider, final Annotation[] annotations) {
            return new MyResourceProvider();
        }
    }

    @Test
    public void testTypeAnnotationProcessor() {
        try (final MinijaxInjector injector = new MinijaxInjector()) {
            injector.addTypeAnnotationProcessor(MyTypeAnnotation.class, new MyTypeAnnotationProcessor());

            final MyTestResource resource = injector.getResource(MyTestResource.class);
            assertEquals("baz", resource.foo);
        }
    }
}
