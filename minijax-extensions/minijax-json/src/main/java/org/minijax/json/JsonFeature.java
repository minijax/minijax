package org.minijax.json;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class JsonFeature implements Feature {

    @Override
    public boolean configure(final FeatureContext context) {
        context.register(MinijaxObjectMapper.getInstance());
        context.register(MinijaxJsonReader.class);
        context.register(MinijaxJsonWriter.class);
        context.register(MinijaxJsonExceptionMapper.class);
        return true;
    }
}
