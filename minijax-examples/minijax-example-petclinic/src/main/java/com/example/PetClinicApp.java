package com.example;

import org.minijax.Minijax;
import org.minijax.mustache.MustacheFeature;
import org.minijax.persistence.PersistenceFeature;

class PetClinicApp {

    public static void main(final String[] args) {
        new Minijax()
                .register(PersistenceFeature.class)
                .register(MustacheFeature.class)
                .staticDirectories("static")
                .packages("com.example")
                .start();
    }
}
