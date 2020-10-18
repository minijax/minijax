package org.minijax.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.config.PropertyOrderStrategy;
import jakarta.json.bind.config.PropertyVisibilityStrategy;

public class Json {
    private static Jsonb instance;

    Json() {
        throw new UnsupportedOperationException();
    }

    public static Jsonb getObjectMapper() {
        if (instance == null) {
            final PropertyVisibilityStrategy visibility = new PropertyVisibilityStrategy() {
                @Override
                public boolean isVisible(final Field field) {
                    return true;
                }

                @Override
                public boolean isVisible(final Method method) {
                    return false;
                }
            };

            final JsonbConfig config = new JsonbConfig()
                    .withPropertyVisibilityStrategy(visibility)
                    .withPropertyOrderStrategy(PropertyOrderStrategy.LEXICOGRAPHICAL);

            instance = JsonbBuilder.create(config);
        }
        return instance;
    }
}
