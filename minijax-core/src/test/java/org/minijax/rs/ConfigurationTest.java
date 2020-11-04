package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;

class ConfigurationTest {

    @Test
    void testProperty() {
        final Minijax container = new Minijax();
        container.property("a", "b");
        assertEquals("b", container.getDefaultApplication().getConfiguration().getProperty("a"));
    }

    @Test
    void testPropertiesFile() throws IOException {
        final Minijax container = new Minijax();
        container.properties(new File("src/test/resources/config.properties"));
        assertEquals("b", container.getDefaultApplication().getConfiguration().getProperty("a"));
    }

    @Test
    void testPropertiesResource() throws IOException {
        final Minijax container = new Minijax();
        container.properties("config.properties");
        assertEquals("b", container.getDefaultApplication().getConfiguration().getProperty("a"));
    }

    @Test
    void testDiscoverFile() throws IOException {
        final Minijax container = new Minijax();
        container.properties("src/test/resources/config.properties");
        assertEquals("b", container.getDefaultApplication().getConfiguration().getProperty("a"));
    }
}
