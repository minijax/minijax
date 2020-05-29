package com.example;

import static jakarta.ws.rs.core.MediaType.*;

import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import org.minijax.Minijax;
import org.minijax.mustache.MustacheFeature;
import org.minijax.view.View;

@Path("/")
@Produces(TEXT_HTML)
public class HelloMustache {

    @GET
    public static View hello() {
        final Map<String, Object> model = new HashMap<>();
        model.put("name", "Chris");
        model.put("value", 10000);
        model.put("taxed_value", 10000 - (10000 * 0.4));
        model.put("in_ca", true);

        return new View("demo", model);
    }

    public static void main(final String[] args) {
        new Minijax()
                .register(MustacheFeature.class)
                .register(HelloMustache.class)
                .start();
    }
}
