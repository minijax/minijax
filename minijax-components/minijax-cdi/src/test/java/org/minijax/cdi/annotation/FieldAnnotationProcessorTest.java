package org.minijax.cdi.annotation;

import static org.junit.jupiter.api.Assertions.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Test;
import org.minijax.cdi.MinijaxInjector;
import org.minijax.cdi.MinijaxInjectorState;
import org.minijax.cdi.MinijaxProvider;

class FieldAnnotationProcessorTest {

    @Target({ FIELD })
    @Retention(RUNTIME)
    public @interface MyFieldAnnotation {
    }

    public static class MyTestResource {
        @MyFieldAnnotation
        Object foo;
    }

    public static class MyObjectProvider implements MinijaxProvider<Object> {

        @Override
        public Object get(final Object context) {
            return "hello";
        }
    }

    public static class MyFieldAnnotationProcessor implements FieldAnnotationProcessor<Object> {

        @Override
        public MinijaxProvider<Object> buildProvider(final MinijaxInjectorState state, final Class<Object> type, final Annotation[] annotations) {
            return new MyObjectProvider();
        }
    }

    @Test
    void testFieldAnnotationProcessor() {
        try (final MinijaxInjector injector = new MinijaxInjector()) {
            injector.addFieldAnnotationProcessor(MyFieldAnnotation.class, new MyFieldAnnotationProcessor());

            final MyTestResource resource = injector.getResource(MyTestResource.class);
            assertEquals("hello", resource.foo);
        }
    }
}
