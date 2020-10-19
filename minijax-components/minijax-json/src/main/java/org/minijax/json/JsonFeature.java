package org.minijax.json;

import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;

public class JsonFeature implements Feature {

    @Override
    public boolean configure(final FeatureContext context) {
        context.register(MinijaxJsonReader.class);
        context.register(MinijaxJsonWriter.class);
        context.register(MinijaxJsonExceptionMapper.class);
        return true;
    }
}
