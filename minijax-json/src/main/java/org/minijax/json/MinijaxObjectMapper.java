package org.minijax.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

public class MinijaxObjectMapper {
    private static ObjectMapper instance;

    public static ObjectMapper getInstance() {
        if (instance == null) {
            instance = new ObjectMapper();
            instance.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            instance.setSerializationInclusion(Include.NON_NULL);
            instance.registerModule(new JaxbAnnotationModule());
        }
        return instance;
    }
}
