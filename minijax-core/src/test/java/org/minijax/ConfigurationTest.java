package org.minijax;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ConfigurationTest {

    @Test
    public void testProperty() {
        final Minijax container = new Minijax();
        container.property("a", "b");
        assertEquals("b", container.getDefaultApplication().getConfiguration().getProperty("a"));
    }

    @Test
    public void testPropertiesFile() throws IOException {
        final Minijax container = new Minijax();
        container.properties(new File("src/test/resources/config.properties"));
        assertEquals("b", container.getDefaultApplication().getConfiguration().getProperty("a"));
    }
}
