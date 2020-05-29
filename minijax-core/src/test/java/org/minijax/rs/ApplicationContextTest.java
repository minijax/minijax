package org.minijax.rs;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;

import org.junit.Before;
import org.junit.Test;
import org.minijax.Minijax;
import org.minijax.rs.MinijaxApplicationContext;

public class ApplicationContextTest {
    private Minijax minijax;
    private MinijaxApplicationContext app;

    @Before
    public void setUp() {
        minijax = new Minijax();
        app = minijax.getDefaultApplication();
    }

    @Test
    public void testDefaultValues() throws Exception {
        assertEquals(RuntimeType.SERVER, app.getRuntimeType());
        assertNotNull(app.getProperties());
        assertNotNull(app.getPropertyNames());
        assertNotNull(app.getInstances());
        assertNotNull(app.getWebSockets());
    }

    @Test
    public void testSetProperty() {
        app.property("foo", "bar");
        assertEquals("bar", app.getProperty("foo"));
    }

    @Test
    public void testProperties() {
        final Properties props = new Properties();
        props.put("foo", "bar");
        minijax.properties(props);
        assertEquals("bar", app.getProperty("foo"));
    }

    @Test
    public void testPropertiesMap() {
        final Map<String, String> props = new HashMap<>();
        props.put("foo", "bar");
        minijax.properties(props);
        assertEquals("bar", app.getProperty("foo"));
    }

    @Test
    public void testPropertiesFile() throws Exception {
        minijax.properties(ApplicationContextTest.class.getClassLoader().getResourceAsStream("config.properties"));
        assertEquals("b", app.getProperty("a"));
    }

    @Test
    public void testScanPackage() {
        minijax.packages("org.minijax");
        assertNotNull(app.getClasses());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsEnabled1() {
        app.isEnabled(new MyFeature());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsEnabled2() {
        app.isEnabled(MyFeature.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsRegistered1() {
        app.isRegistered(new MyFeature());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsRegistered2() {
        app.isRegistered(MyFeature.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetContracts() {
        app.getContracts(MyFeature.class);
    }

    public static class MyFeature implements Feature {
        @Override
        public boolean configure(final FeatureContext context) {
            return false;
        }
    }
}
