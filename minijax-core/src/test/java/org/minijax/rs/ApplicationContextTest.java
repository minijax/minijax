package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.Minijax;

class ApplicationContextTest {
    private Minijax minijax;
    private MinijaxApplicationContext app;

    @BeforeEach
    public void setUp() {
        minijax = new Minijax();
        app = minijax.getDefaultApplication();
    }

    @Test
    void testDefaultValues() throws Exception {
        assertEquals(RuntimeType.SERVER, app.getRuntimeType());
        assertNotNull(app.getProperties());
        assertNotNull(app.getPropertyNames());
        assertNotNull(app.getInstances());
        assertNotNull(app.getWebSockets());
    }

    @Test
    void testSetProperty() {
        app.property("foo", "bar");
        assertEquals("bar", app.getProperty("foo"));
    }

    @Test
    void testProperties() {
        final Properties props = new Properties();
        props.put("foo", "bar");
        minijax.properties(props);
        assertEquals("bar", app.getProperty("foo"));
    }

    @Test
    void testPropertiesMap() {
        final Map<String, String> props = new HashMap<>();
        props.put("foo", "bar");
        minijax.properties(props);
        assertEquals("bar", app.getProperty("foo"));
    }

    @Test
    void testPropertiesFile() throws Exception {
        minijax.properties(ApplicationContextTest.class.getClassLoader().getResourceAsStream("config.properties"));
        assertEquals("b", app.getProperty("a"));
    }

    @Test
    void testBindSingleton() {
        final Widget singleton = new Widget();
        minijax.bind(singleton, Widget.class);
        assertSame(singleton, minijax.getResource(Widget.class));
    }

    @Test
    void testBindClass() {
        minijax.bind(Widget.class, Widget.class);
        final Widget w1 = minijax.getResource(Widget.class);
        final Widget w2 = minijax.getResource(Widget.class);
        assertNotSame(w1, w2);
    }

    @Test
    void testScanPackage() {
        minijax.packages("org.minijax");
        assertNotNull(app.getClasses());
    }

    @Test
    void testIsEnabled1() {
        assertThrows(UnsupportedOperationException.class, () -> app.isEnabled(new MyFeature()));
    }

    @Test
    void testIsEnabled2() {
        assertThrows(UnsupportedOperationException.class, () -> app.isEnabled(MyFeature.class));
    }

    @Test
    void testIsRegistered1() {
        assertThrows(UnsupportedOperationException.class, () -> app.isRegistered(new MyFeature()));
    }

    @Test
    void testIsRegistered2() {
        assertThrows(UnsupportedOperationException.class, () -> app.isRegistered(MyFeature.class));
    }

    @Test
    void testGetContracts() {
        assertThrows(UnsupportedOperationException.class, () -> app.getContracts(MyFeature.class));
    }

    public static class MyFeature implements Feature {
        @Override
        public boolean configure(final FeatureContext context) {
            return false;
        }
    }
}
