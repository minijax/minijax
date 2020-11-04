package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;

class MultipleApplicationsTest {

    @ApplicationPath("/app1")
    public static class MyApp1 extends Application {

        @Override
        public Map<String, Object> getProperties() {
            final Map<String, Object> properties = new HashMap<>();
            properties.put("k", "v1");
            return properties;
        }
    }

    @ApplicationPath("/app2")
    public static class MyApp2 extends Application {

        @Override
        public Map<String, Object> getProperties() {
            final Map<String, Object> properties = new HashMap<>();
            properties.put("k", "v2");
            return properties;
        }
    }

    @Test
    void testApp() {
        final Minijax minijax = new Minijax();
        minijax.register(MyApp1.class);
        minijax.register(MyApp2.class);

        assertEquals(2, minijax.getApplications().size());
        assertEquals("v1", minijax.getApplication(URI.create("/app1")).getProperty("k"));
        assertEquals("v2", minijax.getApplication(URI.create("/app2")).getProperty("k"));
    }
}
