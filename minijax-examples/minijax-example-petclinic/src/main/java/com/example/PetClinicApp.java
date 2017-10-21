package com.example;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.minijax.Minijax;
import org.minijax.data.DataProperties;
import org.minijax.mustache.MinijaxMustacheFeature;

public class PetClinicApp {

    public static void main(final String[] args) {
        final Map<String, String> props = new HashMap<>();
        props.put(DataProperties.DRIVER, "org.h2.Driver");
        props.put(DataProperties.URL, "jdbc:h2:~/.minijax-petclinic/database");
        props.put(DataProperties.USERNAME, "");

        new Minijax()
                .register(Persistence.createEntityManagerFactory("petclinic", props), EntityManagerFactory.class)
                .register(MinijaxMustacheFeature.class)
                .addStaticDirectory("static")
                .packages("com.example")
                .run(8080);
    }
}
