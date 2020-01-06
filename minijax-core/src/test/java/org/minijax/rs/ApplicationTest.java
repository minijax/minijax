package org.minijax.rs;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.junit.Test;
import org.minijax.Minijax;
import org.minijax.rs.MinijaxApplicationContext;

public class ApplicationTest {

    public static class MyComponent1 {
        public int x;
    }

    public static class MyComponent2 {
        public int x;

        public MyComponent2(final int x) {
            this.x = x;
        }
    }

    @ApplicationPath("/myapp")
    public static class MyApp extends Application {

        @Override
        public Map<String, Object> getProperties() {
            final Map<String, Object> properties = new HashMap<>();
            properties.put("k", "v");
            return properties;
        }

        @Override
        public Set<Class<?>> getClasses() {
            final Set<Class<?>> classes = new HashSet<>();
            classes.add(MyComponent1.class);
            return classes;
        }

        @Override
        public Set<Object> getSingletons() {
            final Set<Object> singletons = new HashSet<>();
            singletons.add(new MyComponent2(42));
            return singletons;
        }
    }

    @Test
    public void testApp() {
        final Minijax minijax = new Minijax();
        minijax.register(MyApp.class);

        assertEquals(1, minijax.getApplications().size());

        final MinijaxApplicationContext app = minijax.getDefaultApplication();
        assertEquals("v", app.getProperty("k"));
        assertEquals(42, app.getResource(MyComponent2.class).x);
    }
}