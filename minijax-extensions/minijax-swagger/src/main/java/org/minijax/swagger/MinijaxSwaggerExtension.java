package org.minijax.swagger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.FormParam;

import org.minijax.multipart.Part;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs.ext.AbstractSwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.Property;
import io.swagger.util.Json;

/**
 * Swagger extension for handling JAX-RS 2.0 processing.
 */
public class MinijaxSwaggerExtension extends AbstractSwaggerExtension {
    final ObjectMapper mapper = Json.mapper();

    @Override
    public List<Parameter> extractParameters(
            final List<Annotation> annotations,
            final Type type,
            final Set<Type> typesToSkip,
            final Iterator<SwaggerExtension> chain) {

        if (shouldIgnoreType(type, typesToSkip)) {
            return Collections.emptyList();
        }

        final List<Parameter> parameters = new ArrayList<>();

        for (final Annotation annotation : annotations) {
            if (annotation instanceof FormParam) {
                final FormParam fd = (FormParam) annotation;

                if (java.io.InputStream.class.isAssignableFrom(constructType(type).getRawClass())) {
                    parameters.add(new FormParameter().type("file").name(fd.value()));

                } else if (Part.class.isAssignableFrom(constructType(type).getRawClass())) {
                    parameters.add(new FormParameter().type("file").name(fd.value()));

                } else {
                    final FormParameter fp = new FormParameter().name(fd.value());
                    final Property schema = ModelConverters.getInstance().readAsProperty(type);
                    if (schema != null) {
                        fp.setProperty(schema);
                    }
                    parameters.add(fp);
                }
            }
        }

        if (!parameters.isEmpty()) {
            return parameters;
        }

        // Only call down to the other items in the chain if no parameters were produced
        return super.extractParameters(annotations, type, typesToSkip, chain);
    }
}
