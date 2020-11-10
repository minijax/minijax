package org.minijax.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.config.PropertyOrderStrategy;
import jakarta.json.bind.config.PropertyVisibilityStrategy;

/**
 * The Json class provides helper utilities for creating the singleton object mapper.
 *
 * For most use cases, you can simply call getObjectMapper() without anything extra.
 *
 * You can instantiate the singleton object mapper with a custom configuration using createObjectMapper().
 */
public class Json {
    private static Jsonb instance;

    Json() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the default JsonbConfig configuration.
     *
     * In addition to the standard Jsonb defaults:
     *     1) Uses field visibility
     *     2) Sorts lexicographically
     *
     * @return The default JsonbConfig.
     */
    public static JsonbConfig getDefaultConfig() {
        final PropertyVisibilityStrategy visibility = new PropertyVisibilityStrategy() {
            @Override
            public boolean isVisible(final Field field) {
                return true;
            }

            @Override
            public boolean isVisible(final Method method) {
                return method.getAnnotation(JsonbProperty.class) != null;
            }
        };

        return new JsonbConfig()
                .withPropertyVisibilityStrategy(visibility)
                .withPropertyOrderStrategy(PropertyOrderStrategy.LEXICOGRAPHICAL);
    }

    /**
     * Initializes the object mapper.
     *
     * Can only be called once.  Cannot be called after getObjectMapper().
     *
     * If you need a custom configuration, you should call this method once at the
     * beginning of your application.
     *
     * @param config The object mapper configuration.
     */
    public static void initObjectMapper(final JsonbConfig config) {
        if (instance != null) {
            throw new IllegalStateException("JSON object mapper already created");
        }
        instance = JsonbBuilder.create(config);
    }

    /**
     * Returns the object mapper singleton.
     *
     * @return The object mapper singleton.
     */
    public static Jsonb getObjectMapper() {
        if (instance == null) {
            initObjectMapper(getDefaultConfig());
        }
        return instance;
    }
}
