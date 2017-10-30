package com.example;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.minijax.MinijaxProperties;
import org.minijax.Minijax;
import org.minijax.mustache.MinijaxMustacheFeature;

public class PetClinicApp {

    public static void main(final String[] args) {
        final Map<String, String> props = new HashMap<>();
        props.put(MinijaxProperties.DB_DRIVER, "org.h2.Driver");
        props.put(MinijaxProperties.DB_URL, "jdbc:h2:~/.minijax-petclinic/database");
        props.put(MinijaxProperties.DB_USERNAME, "");

        new Minijax()
                .register(Persistence.createEntityManagerFactory("petclinic", props), EntityManagerFactory.class)
                .register(MinijaxMustacheFeature.class)
                .addStaticDirectory("static")
                .packages("com.example")
                .run(8080);
    }
}
