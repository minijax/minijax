package com.example;

import org.minijax.MinijaxProperties;
import org.minijax.mustache.MustacheFeature;
import org.minijax.test.MinijaxTest;

public abstract class PetClinicTest extends MinijaxTest {
    protected PetClinicTest() {
        getServer()
            .property(MinijaxProperties.DB_DRIVER, "org.h2.Driver")
            .property(MinijaxProperties.DB_URL, "jdbc:h2:mem:test")
            .registerPersistence()
            .register(MustacheFeature.class);
    }
}
