package com.example;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.minijax.Minijax;
import org.minijax.mustache.MinijaxMustacheFeature;

public class PetClinicApp {

    public static void main(final String[] args) {
        new Minijax()
                .register(Persistence.createEntityManagerFactory("petclinic"), EntityManagerFactory.class)
                .register(MinijaxMustacheFeature.class)
                .packages("com.example")
                .run(8080);
    }
}
