package org.minijax.json;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

public class MinijaxJsonFeature implements Feature {

    @Override
    public boolean configure(final FeatureContext context) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.registerModule(new JaxbAnnotationModule());
        context.register(mapper);

        context.register(MinijaxJsonReader.class);
        context.register(MinijaxJsonWriter.class);
        context.register(MinijaxJsonExceptionMapper.class);
        return true;
    }
}
