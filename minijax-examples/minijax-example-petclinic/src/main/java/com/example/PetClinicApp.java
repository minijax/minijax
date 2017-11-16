package com.example;

import org.minijax.Minijax;
import org.minijax.mustache.MustacheFeature;

class PetClinicApp {

    public static void main(final String[] args) {
        new Minijax()
                .registerPersistence()
                .register(MustacheFeature.class)
                .addStaticDirectory("static")
                .packages("com.example")
                .run(8080);
    }
}
