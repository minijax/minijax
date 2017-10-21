package com.example;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.minijax.Minijax;
import org.minijax.mustache.MinijaxMustacheFeature;

public class PetClinicApp {

    public static void main(final String[] args) {
        final Map<String, String> props = new HashMap<>();
        props.put("org.minijax.data.driver", "org.h2.Driver");
        props.put("org.minijax.data.url", "jdbc:h2:~/.minijax-petclinic/database");
        props.put("org.minijax.data.user", "");

        new Minijax()
                .register(Persistence.createEntityManagerFactory("petclinic", props), EntityManagerFactory.class)
                .register(MinijaxMustacheFeature.class)
                .addStaticDirectory("static")
                .packages("com.example")
                .run(8080);
    }
}
