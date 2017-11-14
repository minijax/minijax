package org.minijax.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

public class Json {
    private static ObjectMapper instance;

    Json() {
        throw new UnsupportedOperationException();
    }

    public static ObjectMapper getObjectMapper() {
        if (instance == null) {
            instance = new ObjectMapper();
            instance.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            instance.setSerializationInclusion(Include.NON_NULL);
            instance.registerModule(new JaxbAnnotationModule());
            instance.registerModule(new JavaTimeModule());
            instance.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        }
        return instance;
    }
}
