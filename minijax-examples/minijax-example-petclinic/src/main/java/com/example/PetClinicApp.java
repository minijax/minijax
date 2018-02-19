package com.example;

import org.minijax.Minijax;
import org.minijax.db.PersistenceFeature;
import org.minijax.mustache.MustacheFeature;

class PetClinicApp {

    public static void main(final String[] args) {
        new Minijax()
                .register(PersistenceFeature.class)
                .register(MustacheFeature.class)
                .staticDirectories("static")
                .packages("com.example")
                .start(8080);
    }
}
