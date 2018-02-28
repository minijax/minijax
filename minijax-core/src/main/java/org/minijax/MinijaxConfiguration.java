package org.minijax;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinijaxConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(MinijaxConfiguration.class);
    private final Map<String, Object> properties;

    public MinijaxConfiguration() {
        properties = new HashMap<>();
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Object get(final String key) {
        return properties.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrDefault(final String key, final T defaultValue) {
        return (T) properties.getOrDefault(key, defaultValue);
    }

    public void property(final String name, final Object value) {
        properties.put(name, value);
    }

    public void properties(final Map<String, String> props) {
        properties.putAll(props);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void properties(final Properties props) {
        properties.putAll((Map) props);
    }

    public void properties(final File file) throws IOException {
        final Properties props = new Properties();
        try (final FileReader r = new FileReader(file)) {
            props.load(r);
        }
        properties(props);
    }

    public void properties(final InputStream inputStream) throws IOException {
        final Properties props = new Properties();
        props.load(inputStream);
        properties(props);
    }

    public void properties(final String fileName) throws IOException {
        final File file = new File(fileName);
        if (file.exists()) {
            properties(file);
            return;
        }

        try (final InputStream in = Minijax.class.getClassLoader().getResourceAsStream(fileName)) {
            if (in != null) {
                properties(in);
                return;
            }
        }

        LOG.warn("Properties file \"{}\" not found", fileName);
    }
}
